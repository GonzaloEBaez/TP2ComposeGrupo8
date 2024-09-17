package com.tp2compose.data.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.tp2compose.data.entities.Ciudad
import com.tp2compose.data.entities.Pais

data class CiudadPais(
    @Embedded val ciudad: Ciudad,
    @Relation(
        parentColumn = "idPais",
        entityColumn = "id"
    )
    val pais: Pais
)
