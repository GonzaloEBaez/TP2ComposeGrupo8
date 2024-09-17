package com.tp2compose.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pais")
data class Pais(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val nombre: String
)
