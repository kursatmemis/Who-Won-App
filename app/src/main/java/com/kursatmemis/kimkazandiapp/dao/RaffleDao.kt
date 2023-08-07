package com.kursatmemis.kimkazandiapp.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.kursatmemis.kimkazandiapp.models.Raffle

@Dao
interface RaffleDao {

    @Insert
    fun insert(raffle: Raffle): Long

    @Query("SELECT EXISTS(SELECT 1 FROM raffle WHERE clickPageUrl = :url LIMIT 1)")
    fun existsByUrl(url: String): Boolean

    @Query("DELETE FROM raffle WHERE clickPageUrl = :url")
    fun deleteByURL(url: String)

    @Query("Delete From raffle")
    fun deleteAll(): Int

    @Query("Select * from raffle")
    fun getAll(): List<Raffle>

    @Query("SELECT * FROM raffle WHERE isFollow = 1")
    fun getFollowedRaffle(): List<Raffle>

    @Query("SELECT * FROM raffle WHERE url = :url")
    fun getRafflesByUrl(url: String): List<Raffle>

    @Query("UPDATE raffle SET isFollow = :isFollow WHERE id = :raffleId")
    fun updateRaffleFollowStatus(raffleId: Int, isFollow: Boolean)

}