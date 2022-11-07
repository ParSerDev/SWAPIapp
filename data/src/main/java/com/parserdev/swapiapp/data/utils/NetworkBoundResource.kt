package com.parserdev.swapiapp.data.utils

import com.parserdev.swapiapp.domain.UNABLE_TO_CONNECT
import com.parserdev.swapiapp.domain.network.NetworkResult
import kotlinx.coroutines.flow.*

inline fun <ResultType, RequestType> networkBoundResource(
    crossinline query: () -> Flow<ResultType>,
    crossinline fetch: suspend () -> RequestType,
    crossinline saveFetchResult: suspend (RequestType) -> Unit,
    crossinline shouldFetch: (ResultType) -> Boolean = { true }
) = flow {
    val data = query().first()

    val flow = if (shouldFetch(data)) {
        emit(NetworkResult.Loading(data))
        try {
            saveFetchResult(fetch())
            query().map { NetworkResult.Success(it) }
        } catch (throwable: Throwable) {
            query().map { NetworkResult.Error(UNABLE_TO_CONNECT, it) }
        }
    } else {
        query().map { NetworkResult.Success(it) }
    }

    emitAll(flow)
}