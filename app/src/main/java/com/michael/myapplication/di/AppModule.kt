package com.michael.myapplication.di

import com.michael.myapplication.data.remote.ApiService
import com.michael.myapplication.data.remote.repository.RestaurantRepositoryImpl
import com.michael.myapplication.ui.restaurant.RestaurantListViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

const val API_BASE_URL = "https://eccdn.com.au/"

val appModule = module {
    single {
        Retrofit.Builder()
            .baseUrl(API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    factory { get<Retrofit>().create(ApiService::class.java) }
    single { RestaurantRepositoryImpl(get()) }
    viewModel {
        RestaurantListViewModel(get<RestaurantRepositoryImpl>())
    }
}