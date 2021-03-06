package com.example.tasks.service.repository.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.tasks.service.model.PriorityModel

@Dao
interface PriorityDAO {
    @Insert
    fun save(List:List<PriorityModel>)

    @Query("SELECT * FROM priority")
    fun list():List<PriorityModel>

    @Query("SELECT descrpition FROM priority WHERE id = :id")
            fun getDescription(id:Int):String

    @Query("DELETE FROM priority WHERE 1=1")
    fun clear()
}