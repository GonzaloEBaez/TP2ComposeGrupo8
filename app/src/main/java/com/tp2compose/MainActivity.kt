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
        Button(onClick = {
            val intent = Intent(context, ABMCiudadP2::class.java)
            context.startActivity(intent)
        },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Blue , // Color del botón
                contentColor = Color.White // Color del texto
            )
        )
        {
            Text(text = "Crear capitales!")
        }
    }
}
@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    Inicio()
}


