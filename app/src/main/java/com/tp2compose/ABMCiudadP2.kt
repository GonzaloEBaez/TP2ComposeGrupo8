package com.tp2compose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tp2compose.ui.theme.TP2ComposeTheme
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.launch
import androidx.compose.foundation.clickable
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem

class ABMCiudadP2 : ComponentActivity() {
    private lateinit var dbHelper: CiudadDatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Inicializar la base de datos
        dbHelper = CiudadDatabaseHelper(this)
        dbHelper.eliminarTodosLosPaises()
        dbHelper.initializeDatabase()

        setContent {
            TP2ComposeTheme {
                ABMCiudadScreen()
            }
        }
    }
}

@Composable
fun ABMCiudadScreen() {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val dbHelper = CiudadDatabaseHelper(context)

    var ciudadNombre by remember { mutableStateOf("") }
    var paisNombre by remember { mutableStateOf("") }
    var poblacion by remember { mutableStateOf("") }
    var mensaje by remember { mutableStateOf("") }

    // Estado para el menú desplegable
    var expanded by remember { mutableStateOf(false) }
    var paises by remember { mutableStateOf(listOf<String>()) }

    // Llamada para cargar los nombres de los países
    LaunchedEffect(Unit) {
        paises = dbHelper.obtenerNombresPaises()
    }

    // Función para limpiar todos los campos
    fun limpiarCampos() {
        ciudadNombre = ""
        paisNombre = ""
        poblacion = ""
        mensaje = "Campos limpiados"
    }

    fun saveCiudad() {
        scope.launch {
            val nombreCiudad = ciudadNombre
            val nombrePais = paisNombre
            val poblacionCiudad = poblacion.toIntOrNull()

            if (nombreCiudad.isNotEmpty() && nombrePais.isNotEmpty() && poblacionCiudad != null) {
                val paisId = dbHelper.getPaisId(nombrePais)
                if (paisId != null) {
                    dbHelper.insertCiudad(nombreCiudad, poblacionCiudad, paisId)
                    mensaje = "Ciudad guardada: $nombreCiudad"
                } else {
                    mensaje = "País no encontrado"
                }
            } else {
                mensaje = "Datos incompletos o inválidos"
            }
        }
    }

    fun consultarCiudad() {
        scope.launch {
            val ciudadInfo = dbHelper.consultarCiudad(ciudadNombre)
            mensaje = ciudadInfo
        }
    }

    fun borrarCiudad() {
        scope.launch {
            val rowsDeleted = dbHelper.borrarCiudad(ciudadNombre)
            mensaje = if (rowsDeleted > 0) "Ciudad borrada" else "Ciudad no encontrada"
        }
    }

    fun borrarCiudadesPorPais() {
        scope.launch {
            val rowsDeleted = dbHelper.borrarCiudadesPorPais(paisNombre)
            mensaje = if (rowsDeleted > 0) "Ciudades borradas" else "País no encontrado"
        }
    }

    fun modificarPoblacion() {
        scope.launch {
            val nuevaPoblacion = poblacion.toIntOrNull()
            if (nuevaPoblacion != null) {
                val rowsUpdated = dbHelper.modificarPoblacion(ciudadNombre, nuevaPoblacion)
                mensaje = if (rowsUpdated > 0) "Población actualizada" else "Ciudad no encontrada"
            } else {
                mensaje = "Población inválida"
            }
        }
    }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
        ) {
            Text(
                text = "Gestión de Ciudad",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 20.dp)
            )

            // TextField de la ciudad
            TextField(
                value = ciudadNombre,
                onValueChange = { newValue -> ciudadNombre = newValue },
                label = { Text("Nombre de la Ciudad") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 10.dp)
            )

            // Dropdown del país
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = "Selecciona un País:", style = MaterialTheme.typography.titleLarge)
                Spacer(modifier = Modifier.height(10.dp))

                Box(modifier = Modifier.fillMaxWidth().padding(bottom = 10.dp)) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { expanded = !expanded }
                            .background(Color.LightGray),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (paisNombre.isEmpty()) "Selecciona un País" else paisNombre,
                            modifier = Modifier
                                .weight(1f)
                                .padding(16.dp)
                        )

                        if (paisNombre.isNotEmpty()) {
                            Text(
                                text = "✖",
                                fontSize = 20.sp,
                                modifier = Modifier
                                    .padding(16.dp)
                                    .clickable {
                                        paisNombre = ""
                                    }
                            )
                        }
                    }

                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        if (paises.isEmpty()) {
                            DropdownMenuItem(
                                onClick = {},
                                text = { Text("No hay países en la BD") }
                            )
                        } else {
                            paises.forEach { pais ->
                                DropdownMenuItem(
                                    text = { Text(pais) },
                                    onClick = {
                                        paisNombre = pais
                                        expanded = false
                                    }
                                )
                            }
                        }
                    }
                }
            }

            // TextField de la población
            TextField(
                value = poblacion,
                onValueChange = { newValue -> poblacion = newValue },
                label = { Text("Población") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 20.dp)
            )

            // Botones con el mismo tamaño
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(
                    onClick = { saveCiudad() },
                    modifier = Modifier.weight(1f).padding(5.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Blue,
                        contentColor = Color.White
                    )
                ) {
                    Text(text = "Guardar Ciudad", fontSize = 16.sp)
                }

                Button(
                    onClick = { consultarCiudad() },
                    modifier = Modifier.weight(1f).padding(5.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Blue,
                        contentColor = Color.White
                    )
                ) {
                    Text(text = "Consultar Ciudad", fontSize = 16.sp)
                }
            }

            Spacer(modifier = Modifier.size(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(
                    onClick = { borrarCiudad() },
                    modifier = Modifier.weight(1f).padding(5.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Blue,
                        contentColor = Color.White
                    )
                ) {
                    Text(text = "Borrar Ciudad", fontSize = 16.sp)
                }

                Button(
                    onClick = { borrarCiudadesPorPais() },
                    modifier = Modifier.weight(1f).padding(5.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Blue,
                        contentColor = Color.White
                    )
                ) {
                    Text(text = "Borrar Ciudades por País", fontSize = 16.sp)
                }
            }

            Spacer(modifier = Modifier.size(10.dp))

            Button(
                onClick = { modificarPoblacion() },
                modifier = Modifier.fillMaxWidth().padding(5.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Blue,
                    contentColor = Color.Black
                )
            ) {
                Text(text = "Modificar Población", fontSize = 16.sp)
            }

            Spacer(modifier = Modifier.size(20.dp))

            Text(
                text = mensaje,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
    }


@Preview(showBackground = true)
@Composable
fun ABMCiudadPreview() {
    TP2ComposeTheme {
        ABMCiudadScreen()
    }
}
