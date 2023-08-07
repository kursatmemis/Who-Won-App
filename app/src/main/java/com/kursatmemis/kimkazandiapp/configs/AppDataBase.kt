package com.kursatmemis.kimkazandiapp.configs

import androidx.room.Database
import androidx.room.RoomDatabase
import com.kursatmemis.kimkazandiapp.dao.RaffleDao
import com.kursatmemis.kimkazandiapp.models.Raffle

@Database(entities = [Raffle::class], version = 1)
abstract class AppDataBase : RoomDatabase() {

    abstract fun raffleDao(): RaffleDao

}