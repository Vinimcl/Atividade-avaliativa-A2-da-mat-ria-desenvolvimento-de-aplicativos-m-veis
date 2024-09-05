// MainActivity.kt
package com.example.jornadadaconquista

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.random.Random

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            JornadaDaConquistaApp()
        }
    }
}

@Composable
fun JornadaDaConquistaApp() {
    var clickCount by remember { mutableStateOf(0) }
    var totalClicks by remember { mutableStateOf(Random.nextInt(1, 51)) }
    var gameEnded by remember { mutableStateOf(false) }
    var currentStage by remember { mutableStateOf(Stage.INITIAL) }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        // Exibe a imagem atual baseada no estágio
        Image(
            painter = painterResource(id = currentStage.imageRes),
            contentDescription = null,
            modifier = Modifier
                .size(300.dp)
                .padding(16.dp)
                .clickable {
                    if (!gameEnded) {
                        clickCount++
                        updateStage(clickCount, totalClicks, onUpdateStage = { stage ->
                            currentStage = stage
                        }, onGameEnd = { gameEnded = true })
                    }
                }
        )

        // Exibe o contador de cliques
        Text(
            text = "Cliques: $clickCount / $totalClicks",
            fontSize = 20.sp,
            modifier = Modifier.padding(8.dp)
        )

        // Botão de Desistir
        Button(
            onClick = {
                currentStage = Stage.GIVE_UP
                gameEnded = true
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
        ) {
            Text("Desistir")
        }

        // Lógica para mostrar as opções quando o jogo acaba
        if (gameEnded) {
            ShowEndOptions(
                onNewGame = {
                    clickCount = 0
                    totalClicks = Random.nextInt(1, 51)
                    currentStage = Stage.INITIAL
                    gameEnded = false
                },
                onExit = {
                    // Finaliza ou retorna para a tela inicial
                }
            )
        }
    }
}

@Composable
fun ShowEndOptions(onNewGame: () -> Unit, onExit: () -> Unit) {
    Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.padding(top = 16.dp)) {
        Button(onClick = onNewGame) {
            Text("Novo Jogo")
        }
        Spacer(modifier = Modifier.width(8.dp))
        Button(onClick = onExit) {
            Text("Sair")
        }
    }
}

fun updateStage(
    clickCount: Int,
    totalClicks: Int,
    onUpdateStage: (Stage) -> Unit,
    onGameEnd: () -> Unit
) {
    val progress = clickCount.toFloat() / totalClicks
    when {
        progress >= 1.0f -> {
            onUpdateStage(Stage.CONQUEST)
            onGameEnd()
        }
        progress >= 0.66f -> onUpdateStage(Stage.FINAL)
        progress >= 0.33f -> onUpdateStage(Stage.MID)
        else -> onUpdateStage(Stage.INITIAL)
    }
}

enum class Stage(val imageRes: Int) {
    INITIAL(R.drawable.inicio),  // Substitua com o nome correto das suas imagens
    MID(R.drawable.mediano),
    FINAL(R.drawable.finalizando),
    CONQUEST(R.drawable.conquista),
    GIVE_UP(R.drawable.desistencia)
}
