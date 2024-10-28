package com.apps.numbersinfoapp.data.room

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.apps.numbersinfoapp.data.Constants.NUMS_TABLE
import com.apps.numbersinfoapp.domain.model.NumberModel

@Entity(tableName = NUMS_TABLE)
data class NumbersEntity (
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val info: String?
        ){
    fun toModel(): NumberModel{
        return NumberModel(this.id, this.info)
    }
}