package com.michael.myapplication.domain.repository

import com.michael.myapplication.data.DataState
import com.michael.myapplication.domain.model.Restaurant
import kotlinx.coroutines.flow.Flow

interface RestaurantRepository {
    suspend fun getRestaurants(): Flow<DataState<List<Restaurant>>>
}