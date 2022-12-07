package com.example.smb1.Models

import androidx.room.Entity

@Entity
data class dbModel(
    var id: String,
    var price: Double,
    var itemName: String,
    var quantity: Int,
    var bought: Boolean
)
