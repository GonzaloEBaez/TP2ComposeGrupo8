package com.tp2compose

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Inicio()
        }
    }
}

@Composable
fun Inicio() {
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "TP 2 - App Móviles",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier.padding(bottom = 10.dp)
        )
        Button(onClick = {
            val intent = Intent(context, JuegoP1::class.java)
            context.startActivity(intent)
        },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Blue , // Color del botón
                contentColor = Color.White // Color del texto
            )
        )
        {
            Text(text = "Adiviná el número")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { /* Navegar a Juego 2 */ }) {
            Text(text = "Juego 2")
        }
    }
}
@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    Inicio()
}

//import java.util.prefs.Preferences

//val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "score")

//val MAX_SCORE_KEY = intPreferencesKey("best_score")

//usar datastore y room en vez de lo pedido en el tp
//class MainActivity : ComponentActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        //enableEdgeToEdge()
//        setContent {
//            TP2ComposeTheme {
//                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding -> //andamio de la app? permite mostrar barra superior
//                    Greeting(
//                        name = "Android", // Parametro del compose
//                        modifier = Modifier.padding(innerPadding)
//                    )
//                }
//            }
//        }
//    }
//}
//
//@Composable //Una de las funciones básicas, buscarlas. Esta funcion representa un elem de la pantalla
//
//fun Greeting(name: String, modifier: Modifier = Modifier) {
//    Column (
//        verticalArrangement = Arrangement.Center, //Alineacion vertical
//        horizontalAlignment = Alignment.CenterHorizontally, //Alineacion horizontal
//        modifier = Modifier.fillMaxSize() //Tamaño de la pantalla
//    )
//    {Row(
//        horizontalArrangement = Arrangement.Center, //Alineacion horizontal
//        modifier = Modifier.fillMaxSize() //Tamaño de la pantalla
//    ){
//      //  Icon() //Icono
//    }
//        Text(
//            text = "Hello $name!",
//            fontSize = 10.sp, //Recordar importarlo
//            modifier = modifier //Modifica el componente segun como lo necesitemos en el momento
//        )
//        //Nueva funcion, falta indicar como dibujar el elemento
//        Text(
//            text = "Otro Texto!",
//            fontSize = 10.sp, //Recordar importarlo
//            modifier = modifier //Modifica el componente segun como lo necesitemos en el momento
//        )
//    }
//
//}
//
//@Preview(showBackground = true)
//@Composable
//fun GreetingPreview() {
//    TP2ComposeTheme {
//        Greeting("Android")
//    }
//}
////Punto1
//class punto1 : ComponentActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
//        setContent {
//            Column(
//                //verticalArrangement = Arrangement.Center, //Alineacion vertical
//                horizontalAlignment = Alignment.CenterHorizontally, //Alineacion horizontal
//                modifier = Modifier
//                    .fillMaxSize()
//                    .padding(vertical = 50.dp)){
//                Titulo("Puntaje Actual 0")
//                Spacer(modifier = Modifier.size(10.dp))
//                SubTitulo(text ="Puntaje Máximo 0")
//                Spacer(modifier = Modifier.size(10.dp))
//                Row {
//                    for (i in 1..5){
//                    NumericButton(number = i)
//                }
//            }
//                //Tamaño de la pantalla
//
//            }
//        }
//    }
//
//@Composable
//fun Titulo(text: String, modifier: Modifier = Modifier) {
//    Text(text = text)
//    //text = text,
//    //color = color,
//    //modifier = modifier
//}
//@Composable
//fun SubTitulo(text: String, modifier: Modifier = Modifier) {
//    Text(text = text,
//        fontSize = 10.sp,
//        fontWeight = FontWeight.SemiBold,
//        fontStyle = FontStyle.Italic,
//    )
//}
//
//@Composable
//fun NumericButton(number: Int)
//{
//
//}
//}

