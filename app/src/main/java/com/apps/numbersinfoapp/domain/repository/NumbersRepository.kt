package com.apps.numbersinfoapp.domain.repository

import com.apps.numbersinfoapp.data.retrofit.NetworkResult
import com.apps.numbersinfoapp.domain.model.NumberModel
import kotlinx.coroutines.flow.Flow

interface NumbersRepository {

    suspend fun getNumberInfo(num: String): Flow<NetworkResult<String?>>

    suspend fun getRandomInfo(): Flow<NetworkResult<String?>>

    fun getSavedNumbers(): Flow<List<NumberModel>>

    fun getNumberById(id: Int): Flow<NumberModel>
}
