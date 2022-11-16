package com.example.smb1.Models

import androidx.room.ColumnInfo

data class dbModel(
    val price: Float,
    val itemName: String,
    val quantity: Int,
    var bought: Boolean
)
