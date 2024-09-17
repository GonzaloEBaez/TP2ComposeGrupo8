package com.tp2compose.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.tp2compose.data.dao.CiudadDao
import com.tp2compose.data.entities.Ciudad
import com.tp2compose.data.entities.Pais

@Database(entities = [Ciudad::class, Pais::class], version = 1)
abstract class CiudadDatabase : RoomDatabase() {

    abstract fun ciudadDao(): CiudadDao

    companion object {
        @Volatile
        private var INSTANCE: CiudadDatabase? = null

        fun getDatabase(context: Context): CiudadDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    CiudadDatabase::class.java,
                    "ciudad_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
