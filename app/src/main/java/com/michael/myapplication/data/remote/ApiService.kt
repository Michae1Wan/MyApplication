package com.michael.myapplication.data.remote

import retrofit2.Response
import retrofit2.http.GET

interface ApiService {
    @GET("misc/challengedata.json")
    suspend fun getRestaurants(): Response<RestaurantListResponse>
}