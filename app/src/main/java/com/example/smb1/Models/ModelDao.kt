package com.example.smb1.Models

import androidx.room.*

@Dao
interface ModelDao {

    @Insert
    suspend fun save(myModel: ModelDto): Long

    @Update
    fun update(myModel: ModelDto)

    @Update
    fun update(myModels: List<ModelDto>)

    @Delete
    fun delete(myModel: ModelDto)

    @Query("SELECT * FROM model;")
    fun getAll(): List<ModelDto>

    @Query("SELECT * FROM model WHERE id = :id;")
    suspend fun getSelected(id: Long): ModelDto

    @Query("UPDATE model SET price = :price, quantity = :quantity, itemName = :name, bought = :bought WHERE id = :id")
    suspend fun updateFromId(id: Long, price: Float, quantity: Int, name: String, bought: Boolean)
}