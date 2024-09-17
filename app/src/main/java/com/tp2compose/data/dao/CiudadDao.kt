package com.tp2compose.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.tp2compose.data.entities.Ciudad
import com.tp2compose.data.relations.CiudadPais

@Dao
interface CiudadDao {

    @Insert
    suspend fun insertCiudad(ciudad: Ciudad)

    @Query("SELECT * FROM ciudad WHERE nombre = :nombreCiudad")
    suspend fun getCiudadPorNombre(nombreCiudad: String): Ciudad?

    @Transaction
    @Query("SELECT * FROM ciudad WHERE id = :ciudadId")
    suspend fun getCiudadConPais(ciudadId: Int): CiudadPais
}
