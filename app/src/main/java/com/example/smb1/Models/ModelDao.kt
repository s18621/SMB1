package com.example.smb1.Models

import android.view.Display.Mode
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface ModelDao {

    @Insert
    fun save(myModel: ModelDto) : Long

    @Update
    fun update(myModel: ModelDto)

    @Update fun update(myModels: List<ModelDto>)

    @Delete
    fun delete(myModel: ModelDto)

    @Query("SELECT * FROM model;")
    fun getAll() : List<ModelDto>
}