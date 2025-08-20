package com.michael.myapplication.data.remote

import com.google.gson.annotations.SerializedName

data class RestaurantListResponse(
    val restaurants: List<RemoteRestaurant>
)

data class RemoteRestaurant(
    val objectId: String,
    val name: String,
    val address1: String,
    val suburb: String,
    val cuisines: List<String>,
    val imageLink: String,
    @SerializedName("open")
    val openTime: String,
    @SerializedName("close")
    val closeTime: String,
    val deals: List<RemoteDeal>
)

data class RemoteDeal(
    val objectId: String,
    val discount: Int,
    val dineIn: Boolean,
    val lightning: Boolean,
    @SerializedName("open")
    val openTime: String? = null,
    @SerializedName("close")
    val closeTime: String? = null,
    val qtyLeft: Int,
    val start: String? = null,
    val end: String? = null
)