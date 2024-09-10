import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlin.random.Random
import androidx.dataStore.core.DataStore
import androidx.dataStore.preferences.core.edit
import androidx.dataStore.preferences.core.intPreferencesKey
import androidx.dataStore.preferences.preferencesDataStore
import kotlinx.coroutines.launch


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Game()
        }
    }
}

// DataStore for storing scores
val Context.dataStore by preferencesDataStore(name = "score_data")

@Composable
fun Game() {
    val scope = rememberCoroutineScope()

    // Load scores from DataStore
    val maxScoreKey = intPreferencesKey("max_score")
    val currentScoreKey = intPreferencesKey("current_score")

    val maxScoreFlow: Flow<Int> = LocalContext.current.dataStore.data.map { preferences ->
        preferences[maxScoreKey] ?: 0
    }

    val currentScoreFlow: Flow<Int> = LocalContext.current.dataStore.data.map { preferences ->
        preferences[currentScoreKey] ?: 0
    }

    val maxScore by maxScoreFlow.collectAsState(initial = 0)
    var currentScore by remember { mutableStateOf(0) }
    var secretNumber by remember { mutableStateOf(Random.nextInt(1, 6)) }
    var attempts by remember { mutableStateOf(0) }
    var userGuess by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Tu puntaje actual es: $currentScore")
        Text("Tu mejor puntaje es: $maxScore")
        Spacer(modifier = Modifier.height(16.dp))
        TextField(
            value = userGuess,
            onValueChange = { userGuess = it },
            label = { Text("Adivina un número entre 1 y 5") }
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            val guess = userGuess.toIntOrNull()
            if (guess != null && guess in 1..5) {
                if (guess == secretNumber) {
                    currentScore += 10
                    secretNumber = Random.nextInt(1, 6)
                    attempts = 0
                } else {
                    attempts++
                }
                if (attempts >= 5) {
                    currentScore = 0
                    attempts = 0
                }
                // Update max score if needed
                if (currentScore > maxScore) {
                    scope.launch {
                        LocalContext.current.dataStore.edit { preferences ->
                            preferences[maxScoreKey] = currentScore
                        }
                    }
                }
                scope.launch {
                    LocalContext.current.dataStore.edit { preferences ->
                        preferences[currentScoreKey] = currentScore
                    }
                }
            }
        }) {
            Text("Adivinar")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    GuessingGame()
}
