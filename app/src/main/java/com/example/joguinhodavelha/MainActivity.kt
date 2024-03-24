package com.example.joguinhodavelha

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.joguinhodavelha.ui.theme.JoguinhoDaVelhaTheme
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            JoguinhoDaVelhaTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    JogoVelha("Android")
                }
            }
        }
    }
}

@Composable
fun JogoVelha(name: String, modifier: Modifier = Modifier) {

    val tabuleiro = remember {
        mutableStateOf(Array(3) { arrayOfNulls<String>(3) })
    }
    val jogadorAtual = remember {
        mutableStateOf("X")
    }
    val ganhador = remember {
        mutableStateOf<String?>(null)
    }
    val tabuleiroInicial = Array(3) { arrayOfNulls<String>(3) }
    val jogadorInicial = "X"

    val backgroundImage: Painter = painterResource(id = R.drawable.backgroundimg)

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = backgroundImage,
            contentDescription = "Imagem de Fundo",
            modifier = Modifier.fillMaxSize()
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(25.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Jogo da Velha",
                style = MaterialTheme.typography.titleLarge,
                color = Color.Yellow
            )
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .fillMaxWidth()
                    .background(Color.Black.copy(alpha = 0.5f))
            ) {
                Tabuleiro(tabuleiro, jogadorAtual, ganhador)
                InfoJogo(jogadorAtual, ganhador, tabuleiro, tabuleiroInicial, jogadorInicial)
            }
        }
    }
}

@Composable
fun Tabuleiro(tabuleiro: MutableState<Array<Array<String?>>>,
              jogadorAtual: MutableState<String>,
              ganhador: MutableState<String?>
){
    Column (modifier = Modifier
        .padding(5.dp)
    ){
        for (row in 0..2) {
            Row {
                for (col in 0..2) {
                    OutlinedButton(modifier = Modifier
                        .weight(1f)
                        .padding(1.dp),
                        onClick = {
                            if (tabuleiro.value[row][col] == null && ganhador.value == null) {
                                tabuleiro.value[row][col] = jogadorAtual.value
                                jogadorAtual.value = if (jogadorAtual.value == "X") "0" else "X"
                                ganhador.value = verificarGanhador(tabuleiro.value)
                            }
                        },
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text(
                            text = tabuleiro.value[row][col] ?: "",
                            style = MaterialTheme.typography.titleSmall,
                            color = Color.Yellow,
                        )
                    }
                }
            }
        }
        Row (modifier = Modifier
            .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(text = "Jogador Atual: ${jogadorAtual.value}",
                style = MaterialTheme.typography.titleLarge,
                color = Color.Yellow,
                modifier = Modifier.padding(top = 16.dp)
            )
        }
    }
}

@Composable
fun InfoJogo(jogadorAtual: MutableState<String>,
             ganhador: MutableState<String?>,
             tabuleiro: MutableState<Array<Array<String?>>>,
             tabuleiroInicial: Array<Array<String?>>,
             jogadorInicial: String){
    Column (modifier = Modifier
        .fillMaxWidth()
        .padding(5.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ganhador.value?.let {
            Text(
                text = "Jogador $it ganhou!",
                style = MaterialTheme.typography.titleSmall,
                color = Color.Yellow,
                modifier = Modifier.padding(4.dp)
            )
            LaunchedEffect(true) {
                delay(2000)
                tabuleiro.value = tabuleiroInicial
                jogadorAtual.value = jogadorInicial
                ganhador.value = null
            }
        }

    Row(modifier = Modifier
        .fillMaxSize()
        .padding(2.dp),
        horizontalArrangement = Arrangement.Center
    ){
        OutlinedButton(
            onClick = {
                tabuleiro.value = tabuleiroInicial
                jogadorAtual.value = jogadorInicial
                ganhador.value = null
            },
            shape = RoundedCornerShape(10.dp),
        ){
            Text(text = "Jogar Novamente", color = Color.Yellow)
        }
    }
    }
}

fun verificarGanhador(tabuleiro: Array<Array<String?>>): String? {
    for (row in 0..2) {
        if (tabuleiro[row][0] != null && tabuleiro[row][0] == tabuleiro[row][1] && tabuleiro[row][1] == tabuleiro[row][2]) {
            return tabuleiro[row][0]
        }
    }
    for (col in 0..2) {
        if (tabuleiro[0][col] != null && tabuleiro[0][col] == tabuleiro[1][col] && tabuleiro[1][col] == tabuleiro[2][col]) {
            return tabuleiro[0][col]
        }
    }
    if (tabuleiro[0][0] != null && tabuleiro[0][0] == tabuleiro[1][1] && tabuleiro[1][1] == tabuleiro[2][2]) {
        return tabuleiro[0][0]
    }
    if (tabuleiro[0][2] != null && tabuleiro[0][2] == tabuleiro[1][1] && tabuleiro[1][1] == tabuleiro[2][0]) {
        return tabuleiro[0][2]
    }
    return null
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    JoguinhoDaVelhaTheme {
        JogoVelha("Android")
    }
}