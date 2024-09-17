package com.tp2compose.data.entities


import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ciudad")
data class Ciudad(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val nombre: String,
    val poblacion: Int,
    val idPais: Int
)
