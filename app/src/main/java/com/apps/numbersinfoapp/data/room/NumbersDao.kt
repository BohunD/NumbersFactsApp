package com.apps.numbersinfoapp.data.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.apps.numbersinfoapp.data.Constants
import kotlinx.coroutines.flow.Flow

@Dao
interface NumbersDao {

    @Query("SELECT * FROM ${Constants.NUMS_TABLE}")
    fun getAll(): Flow<List<NumbersEntity>>

    @Query("SELECT * FROM ${Constants.NUMS_TABLE} WHERE id =:id")
    fun getNumberInfo(id:Int): Flow<NumbersEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertNumber(num: NumbersEntity)
}