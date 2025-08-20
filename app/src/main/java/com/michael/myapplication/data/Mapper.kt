package com.michael.myapplication.data

import com.michael.myapplication.data.remote.RemoteDeal
import com.michael.myapplication.data.remote.RemoteRestaurant
import com.michael.myapplication.domain.model.Deal
import com.michael.myapplication.domain.model.Restaurant
import timber.log.Timber
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.util.Locale
import kotlin.String

const val TIME_FORMAT = "h:mma"
fun parseTime(time: String?): LocalDateTime? {
    return if (time != null) {
        try {
            val formatter = DateTimeFormatter.ofPattern(TIME_FORMAT, Locale.ENGLISH)
            val localTime = LocalTime.parse(time.uppercase(), formatter)

            LocalDateTime.of(LocalDate.now(), localTime)

        } catch (e: DateTimeParseException) {
            Timber.e(e, "Failed to parse time: $time")

            null
        }
    }
    else null
}

fun RemoteRestaurant.toRestaurant() = Restaurant(
    id = objectId,
    name = name,
    address = address1,
    suburb = suburb,
    cuisines = cuisines,
    imageLink = imageLink,
    openTime = parseTime(openTime),
    closeTime = parseTime(closeTime),
    deals = deals.map { it.toDeal() },
)

fun RemoteDeal.toDeal() = Deal(
    id = objectId,
    discount = discount,
    dineIn = dineIn,
    lightning = lightning,
    openTime = parseTime(openTime),
    closeTime = parseTime(closeTime),
    qtyLeft = qtyLeft,
    start = parseTime(start),
    end = parseTime(end),
)