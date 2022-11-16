package com.example.smb1.Models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "model")
data class ModelDto(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    @ColumnInfo
    val price: Float,
    @ColumnInfo
    val itemName: String,
    @ColumnInfo
    val quantity: Int,
    @ColumnInfo
    var bought: Boolean
) {


}
