package com.tp2compose

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tp2compose.ui.theme.TP2ComposeTheme
import kotlinx.coroutines.launch
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import kotlin.random.Random

// función que crea una instancia de DataStore para almacenar preferencias
val Context.dataStore by preferencesDataStore(name = "score")

class JuegoP1 : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TP2ComposeTheme {
                JuegoNumeros()
            }
        }
    }
}
// Componente composable
@Composable
fun JuegoNumeros() {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var currentScore by remember { mutableStateOf(0) }
    var highScore by remember { mutableStateOf(0) }
    var randomNumber by remember { mutableStateOf(Random.nextInt(1, 6)) }
    var intentos by remember { mutableStateOf(0) }
    var mensaje by remember { mutableStateOf("") }
    val maxIntentos = 4

    // Cargar el mejor puntaje al inicio
    LaunchedEffect(Unit) {
        val dataStore = context.dataStore.data.first()
        highScore = dataStore[intPreferencesKey("best_score")] ?: 0
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {
        // bienvenida
        Text(
            text = "¡Bienvenido al juego!",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 20.dp)
        )
        // puntaje

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .padding(bottom = 40.dp) // Agrega espacio debajo del texto antes de los botones
            ) {
                Text(text = "Puntaje Actual: $currentScore",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold)
                Text(text = "Puntaje Máximo: $highScore",
                    fontSize = 20.sp,
                    fontStyle = FontStyle.Italic)
                Text(
                    text = "Intentos restantes: ${maxIntentos - intentos}",
                    fontSize = 18.sp, fontStyle = FontStyle.Italic,
                    color = if (intentos >= maxIntentos) androidx.compose.ui.graphics.Color.Red else androidx.compose.ui.graphics.Color.Black)
                Spacer(modifier = Modifier.size(20.dp))
                Text(text = mensaje,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = when {
                    mensaje.contains("correctamente") -> androidx.compose.ui.graphics.Color.Green
                    mensaje.contains("incorrecto") -> androidx.compose.ui.graphics.Color.Red
                    else -> androidx.compose.ui.graphics.Color.Black // Default color
                })
            }
        Spacer(modifier = Modifier.size(20.dp))
        Box(
            modifier = Modifier

            //contentAlignment = Alignment.Center // Centrar en ambos ejes
        ) {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                for (i in 1..6) {
                    BotonNumerico(number = i) {
                        if (i == randomNumber) {
                            currentScore += 10
                            intentos = 0
                            mensaje = "¡Adivinaste correctamente! :)"
                            randomNumber = Random.nextInt(1, 6) // Nuevo número aleatorio
                            if (currentScore > highScore) {
                                highScore = currentScore
                                // Guardar el nuevo puntaje más alto
                                scope.launch {
                                    context.dataStore.edit { preferences ->
                                        preferences[intPreferencesKey(
                                            "best_score"
                                        )] = highScore
                                    }
                                }
                            }
                        } else {
                            intentos++
                            mensaje = "Número incorrecto :("
                            if (intentos >= maxIntentos) {
                                currentScore = 0 // Reiniciar puntaje si llega a 5 intentos fallidos
                                intentos = 0
                                mensaje = "Se acabaron los intentos. El puntaje se ha reiniciado."
                                randomNumber = Random.nextInt(1, 6) // Nuevo número aleatorio
                            }
                        }
                    }
                    Spacer(modifier = Modifier.size(5.dp))
                }
            }
        }
    }
}

@Composable
// función que crea los botones numericos
fun BotonNumerico(number: Int, onClick: () -> Unit) {
    Button(onClick = onClick,
        colors = ButtonDefaults.buttonColors(
        containerColor = Color.Blue , // Color del botón
        contentColor = Color.White // Color del texto
    ))
    {
        Text(text = number.toString(), fontSize = 20.sp)
    }
}

@Preview(showBackground = true) //para ver la pantalla en el editor de composiciones
@Composable
fun JuegoNumerosPreview() {
    TP2ComposeTheme {
        JuegoNumeros()
    }
}