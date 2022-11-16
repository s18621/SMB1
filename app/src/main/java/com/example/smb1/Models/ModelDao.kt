package com.example.smb1.Models

import androidx.room.*

@Dao
interface ModelDao {

    @Insert
    fun save(myModel: ModelDto): Long

    @Update
    fun update(myModel: ModelDto)

    @Update
    fun update(myModels: List<ModelDto>)

    @Delete
    fun delete(myModel: ModelDto)

    @Query("SELECT * FROM model;")
    fun getAll(): List<ModelDto>
}