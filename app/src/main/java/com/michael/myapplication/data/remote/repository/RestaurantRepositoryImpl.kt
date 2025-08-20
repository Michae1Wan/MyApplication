package com.michael.myapplication.data.remote.repository

import com.michael.myapplication.data.remote.ApiService
import com.michael.myapplication.data.toRestaurant
import com.michael.myapplication.domain.repository.RestaurantRepository
import com.michael.myapplication.util.dataFlow

class RestaurantRepositoryImpl(private val apiService: ApiService) : RestaurantRepository {
    override suspend fun getRestaurants() = dataFlow(
        { result ->
            result.body()!!.restaurants.map { it.toRestaurant() }
        },
        {
            apiService.getRestaurants()
        }
    )
}