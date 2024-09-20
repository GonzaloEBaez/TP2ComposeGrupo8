package com.tp2compose

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.content.ContentValues

class CiudadDatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "ciudad.db"
        private const val DATABASE_VERSION = 1

        // Tabla País
        private const val TABLE_PAIS = "pais"
        private const val COLUMN_PAIS_ID = "id"
        private const val COLUMN_PAIS_NOMBRE = "nombre"

        // Tabla Ciudad
        private const val TABLE_CIUDAD = "ciudad"
        private const val COLUMN_CIUDAD_ID = "id"
        private const val COLUMN_CIUDAD_NOMBRE = "nombre"
        private const val COLUMN_CIUDAD_POBLACION = "poblacion"
        private const val COLUMN_CIUDAD_PAIS_ID = "idPais"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createPaisTable = ("CREATE TABLE $TABLE_PAIS (" +
                "$COLUMN_PAIS_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$COLUMN_PAIS_NOMBRE TEXT)"
                )
        db?.execSQL(createPaisTable)
        println("Tabla País creada.")

        val createCiudadTable = ("CREATE TABLE $TABLE_CIUDAD (" +
                "$COLUMN_CIUDAD_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$COLUMN_CIUDAD_NOMBRE TEXT, " +
                "$COLUMN_CIUDAD_POBLACION INTEGER, " +
                "$COLUMN_CIUDAD_PAIS_ID INTEGER, " +
                "FOREIGN KEY($COLUMN_CIUDAD_PAIS_ID) REFERENCES $TABLE_PAIS($COLUMN_PAIS_ID))"
                )
        db?.execSQL(createCiudadTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_CIUDAD")
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_PAIS")
        onCreate(db)
    }

    //Inicializar BD
    fun initializeDatabase() {
        val db = writableDatabase
        insertarDatosIniciales()
        //eliminarTodasLasCiudades()
        // obtenerNombresCiudades()
    }

    // Inserta datos iniciales
    fun insertarDatosIniciales() {
        val db = writableDatabase
        val paisesExistentes = obtenerNombresPaises()

        // Solo insertar si no hay países existentes
        if (paisesExistentes.isEmpty()) {
            val paises = listOf("Argentina", "Brasil", "Chile", "Uruguay", "Paraguay")

            for (pais in paises) {
                val values = ContentValues().apply {
                    put(COLUMN_PAIS_NOMBRE, pais)
                }
                val result = db.insert(TABLE_PAIS, null, values)
                if (result == -1L) {
                    println("Error al insertar el país: $pais")
                } else {
                    println("Insertado el país: $pais con ID: $result")
                }
            }
            // Mensaje de depuración
            println("Datos iniciales insertados.")
        } else {
            println("Los países ya están insertados: $paisesExistentes")
        }
    }

    // Función para eliminar todos los países
    fun eliminarTodosLosPaises() {
        val db = writableDatabase
        db.delete(TABLE_PAIS, null, null)  // Borrar todos los registros de la tabla de países
        println("Todos los países han sido eliminados.")
    }

    // Insertar un país
    fun insertPais(nombre: String) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_PAIS_NOMBRE, nombre)
        }
        db.insert(TABLE_PAIS, null, values)
    }

    // Obtener ID de un país por nombre
    fun getPaisId(nombre: String): Int? {
        val db = readableDatabase
        val query = "SELECT $COLUMN_PAIS_ID FROM $TABLE_PAIS WHERE $COLUMN_PAIS_NOMBRE = ?"
        val cursor = db.rawQuery(query, arrayOf(nombre))
        return if (cursor.moveToFirst()) {
            cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_PAIS_ID))
        } else {
            null
        }.also {
            cursor.close()
        }
    }

    // Insertar una ciudad
    fun insertCiudad(nombre: String, poblacion: Int, paisId: Int) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_CIUDAD_NOMBRE, nombre)
            put(COLUMN_CIUDAD_POBLACION, poblacion)
            put(COLUMN_CIUDAD_PAIS_ID, paisId)
        }
        db.insert(TABLE_CIUDAD, null, values)
    }

    // Consultar una ciudad por nombre
    fun consultarCiudad(nombre: String): String {
        val db = readableDatabase
        val query = """
        SELECT c.$COLUMN_CIUDAD_NOMBRE AS ciudadNombre, c.$COLUMN_CIUDAD_POBLACION, p.$COLUMN_PAIS_NOMBRE AS paisNombre 
        FROM $TABLE_CIUDAD c 
        INNER JOIN $TABLE_PAIS p ON c.$COLUMN_CIUDAD_PAIS_ID = p.$COLUMN_PAIS_ID 
        WHERE c.$COLUMN_CIUDAD_NOMBRE = ?
    """.trimIndent()

        val cursor = db.rawQuery(query, arrayOf(nombre))
        return try {
            if (cursor.moveToFirst()) {
                val ciudadNombre = cursor.getString(cursor.getColumnIndexOrThrow("ciudadNombre"))
                val poblacion = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_CIUDAD_POBLACION))
                val paisNombre = cursor.getString(cursor.getColumnIndexOrThrow("paisNombre"))
                "Ciudad: $ciudadNombre, Población: $poblacion, País: $paisNombre"
            } else {
                "Ciudad no encontrada"
            }
        } catch (e: Exception) {
            "Error al consultar ciudad: ${e.message}"
        } finally {
            cursor.close()
        }
    }

    // Borrar una ciudad por nombre
    fun borrarCiudad(nombre: String): Int {
        val db = writableDatabase
        return db.delete(TABLE_CIUDAD, "$COLUMN_CIUDAD_NOMBRE = ?", arrayOf(nombre))
    }

    // Borrar todas las ciudades de un país
    fun borrarCiudadesPorPais(paisNombre: String): Int {
        val paisId = getPaisId(paisNombre) ?: return 0
        val db = writableDatabase
        return db.delete(TABLE_CIUDAD, "$COLUMN_CIUDAD_PAIS_ID = ?", arrayOf(paisId.toString()))
    }

    // Modificar la población de una ciudad
    fun modificarPoblacion(nombre: String, nuevaPoblacion: Int): Int {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_CIUDAD_POBLACION, nuevaPoblacion)
        }
        return db.update(TABLE_CIUDAD, values, "$COLUMN_CIUDAD_NOMBRE = ?", arrayOf(nombre))
    }

    // Obtener nombres de todos los países
    fun obtenerNombresPaises(): List<String> {
        val db = readableDatabase
        val query = "SELECT $COLUMN_PAIS_NOMBRE FROM $TABLE_PAIS"
        val cursor = db.rawQuery(query, null)
        val paises = mutableListOf<String>()

        val columnIndex = cursor.getColumnIndexOrThrow(COLUMN_PAIS_NOMBRE)

        while (cursor.moveToNext()) {
            val nombre = cursor.getString(columnIndex)
            paises.add(nombre)
        }

        cursor.close()
        println("Paises obtenidos: $paises") // Añadir esta línea para debuguear
        return paises
    }

    // Obtener nombres de todas las ciudades
    fun obtenerNombresCiudades(): List<String> {
        val db = readableDatabase
        val query = "SELECT $COLUMN_CIUDAD_NOMBRE FROM $TABLE_CIUDAD"
        val cursor = db.rawQuery(query, null)
        val ciudades = mutableListOf<String>()

        val columnIndex = cursor.getColumnIndexOrThrow(COLUMN_CIUDAD_NOMBRE)

        while (cursor.moveToNext()) {
            val nombre = cursor.getString(columnIndex)
            ciudades.add(nombre)
        }

        cursor.close()

        // Mostrar las ciudades obtenidas por consola
        println("Ciudades obtenidas: $ciudades") // Añadir esta línea para debuguear
        return ciudades
    }

    // Eliminar todas las ciudades
    fun eliminarTodasLasCiudades() {
        val db = writableDatabase
        db.execSQL("DELETE FROM $TABLE_CIUDAD")
        println("Todas las ciudades han sido eliminadas.")
    }


}
