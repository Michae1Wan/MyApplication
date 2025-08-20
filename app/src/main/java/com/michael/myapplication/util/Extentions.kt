package com.michael.myapplication.util

import com.michael.myapplication.data.DataState
import com.michael.myapplication.data.TIME_FORMAT
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


fun <T> MutableStateFlow<T>.updateState(action: T.() -> T) {
    value = action(value)
}

/**
 * Creates a data flow on [Dispatchers.IO] that that emits loading state before retrieving the data
 * and uses the mapper to convert the received data. If there's any error during the data retrieval
 * an error state is emitted instead.
 */
fun <T, V> dataFlow(mapper: (T) -> V, call: suspend () -> T) : Flow<DataState<V>> {
    return flow {
        emit(DataState.Loading())
        val response = call()
        emit(DataState.Success(mapper.invoke(response)))

    }.catch { e ->
        emit(DataState.Error(e.stackTraceToString()))
    }.flowOn(Dispatchers.IO)
}

fun LocalDateTime.toTimeString(): String {
    return this.format(DateTimeFormatter.ofPattern("h:mma"))
}