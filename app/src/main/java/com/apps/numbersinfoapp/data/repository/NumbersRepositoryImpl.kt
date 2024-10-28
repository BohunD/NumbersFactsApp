package com.apps.numbersinfoapp.data.repository

import android.content.Context
import com.apps.numbersinfoapp.R
import com.apps.numbersinfoapp.data.retrofit.NetworkResult
import com.apps.numbersinfoapp.data.retrofit.NumbersApi
import com.apps.numbersinfoapp.data.room.NumbersDao
import com.apps.numbersinfoapp.data.room.NumbersEntity
import com.apps.numbersinfoapp.domain.model.NumberModel
import com.apps.numbersinfoapp.domain.repository.NumbersRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class NumbersRepositoryImpl(
    private val api: NumbersApi,
    private val dao: NumbersDao,
    private val context: Context
) : NumbersRepository {

    override suspend fun getNumberInfo(num: String): Flow<NetworkResult<String?>> {
        return fetchNumberInfo { api.getNumberInfo(num).execute() }
    }

    override suspend fun getRandomInfo(): Flow<NetworkResult<String?>> {
        return fetchNumberInfo { api.getRandom().execute() }
    }

    private suspend fun fetchNumberInfo(
        request: () -> retrofit2.Response<String?>
    ): Flow<NetworkResult<String?>> = flow {
        emit(NetworkResult.Loading())
        try {
            val response = request()
            if (response.isSuccessful) {
                val info = response.body()
                if (info != null) {
                    saveNumberToDb(info)
                    emit(NetworkResult.Success(info))
                } else {
                    emit(NetworkResult.Error(context.getString(R.string.empty_response_from_api)))
                }
            } else {
                emit(NetworkResult.Error(context.getString(R.string.error, response.message())))
            }
        } catch (e: Exception) {
            emit(NetworkResult.Error(context.getString(R.string.exception, e.message)))
        }
    }

    override fun getSavedNumbers(): Flow<List<NumberModel>> {
        return dao.getAll().map { entities ->
            entities.map { it.toModel() }
        }
    }

    override fun getNumberById(id: Int): Flow<NumberModel> {
        return dao.getNumberInfo(id).map { entity ->
            entity.toModel()
        }
    }

    private fun saveNumberToDb(info: String) {
        dao.insertNumber(NumbersEntity(0, info = info))
    }
}
