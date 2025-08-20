package com.michael.myapplication.domain.model

import java.time.LocalDateTime

data class Restaurant(
    val id: String,
    val name: String,
    val address: String,
    val suburb: String,
    val cuisines: List<String>,
    val imageLink: String,
    val openTime: LocalDateTime?,
    val closeTime: LocalDateTime?,
    val deals: List<Deal>
)

data class Deal(
    val id: String,
    val discount: Int,
    val dineIn: Boolean,
    val lightning: Boolean,
    val openTime: LocalDateTime? = null,
    val closeTime: LocalDateTime? = null,
    val qtyLeft: Int,
    val start: LocalDateTime? = null,
    val end: LocalDateTime? = null
)