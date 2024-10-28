package com.apps.numbersinfoapp.di

import android.content.Context
import androidx.room.Room
import com.apps.numbersinfoapp.data.repository.NumbersRepositoryImpl
import com.apps.numbersinfoapp.domain.repository.NumbersRepository
import com.apps.numbersinfoapp.data.retrofit.ApiService
import com.apps.numbersinfoapp.data.Constants.DB
import com.apps.numbersinfoapp.data.retrofit.NumbersApi
import com.apps.numbersinfoapp.data.room.NumbersDB
import com.apps.numbersinfoapp.data.room.NumbersDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class HiltModule {

    @Provides
    fun provideNumbersDb(@ApplicationContext context: Context) =
        Room.databaseBuilder(context, NumbersDB::class.java, DB).build()

    @Provides
    fun provideNumberDao(numbersDB: NumbersDB) = numbersDB.numbersDao()

    @Provides
    fun provideNumbersApi(): NumbersApi = ApiService.api

    @Provides
    fun provideNumbersRepository(api: NumbersApi, dao: NumbersDao, @ApplicationContext context: Context): NumbersRepository =  NumbersRepositoryImpl(api, dao, context)
}