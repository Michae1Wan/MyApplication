package com.michael.myapplication.ui.restaurant

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.michael.myapplication.data.DataState
import com.michael.myapplication.domain.model.Restaurant
import com.michael.myapplication.domain.repository.RestaurantRepository
import com.michael.myapplication.ui.common.BaseViewState
import com.michael.myapplication.util.updateState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber

class RestaurantListViewModel(
    private val restaurantRepository: RestaurantRepository
) : ViewModel() {
    private val _viewState = MutableStateFlow(RestaurantListViewState())
    val viewState = _viewState.asStateFlow()

    init {
        loadRestaurants()
    }

    fun loadRestaurants() {
        viewModelScope.launch {
            restaurantRepository.getRestaurants().collect { dataState ->
                var isLoading = false
                var error = false
                var restaurants = emptyList<Restaurant>()
                when (dataState) {
                    is DataState.Loading -> {
                        isLoading = true
                        error = false
                    }
                    is DataState.Error -> {
                        isLoading = false
                        error = true
                        Timber.e(dataState.message)
                    }
                    else -> {
                        restaurants = dataState.data?.sortedBy {
                            it.deals.maxOf { deal ->
                                deal.discount
                            }
                        } ?: emptyList()
                    }
                }

                _viewState.updateState {
                    copy(
                        isLoading = isLoading,
                        showError = error,
                        restaurants = restaurants,
                    )
                }
            }
        }
    }
}

data class RestaurantListViewState (
    override val isLoading: Boolean = true,
    override val showError: Boolean = false,
    val restaurants: List<Restaurant> = emptyList(),
): BaseViewState()