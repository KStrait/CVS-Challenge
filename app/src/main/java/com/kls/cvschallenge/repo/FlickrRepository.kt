package com.kls.cvschallenge.repo

import com.kls.cvschallenge.network.WebService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import com.kls.cvschallenge.data.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FlickrRepository @Inject constructor(private val webService: WebService) {
    fun getFlickrItems(search: String): Flow<Result<FlickrResponse>> = flow {
        emit(Result.Loading)
        try {
            val data = webService.getFlickrImages(search)
            emit(Result.Success(data))
        } catch (e: Exception) {
            emit(Result.Error(e))
        }
    }.flowOn(Dispatchers.IO)
}