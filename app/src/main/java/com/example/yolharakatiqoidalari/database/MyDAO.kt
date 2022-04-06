package com.example.yolharakatiqoidalari.database

import androidx.room.*

@Dao
interface MyDAO {

    @Transaction
    @Query("select * from Rules")
    fun getAllRules(): List<Rules>

//    @Query("select * from Rules")
//    fun searchFavourite(favourite: Boolean): List<Rules>

    @Insert
    fun add(rules: Rules)

    @Update
    fun edit(rules: Rules)

    @Delete
    fun delete(rules: Rules)
}