package sk.upjs.wordsup

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.*
import sk.upjs.wordsup.ui.theme.WordsUpTheme
import kotlin.math.roundToInt

class TargetActivity : ComponentActivity() {

    private var target = 20

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainContent()
        }
    }

    @Composable
    private fun MainContent() {
        WordsUpTheme {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.background
            ) {
                Column {
                    Text()
                    Slider()
                    FinishButton()
                }

            }
        }
    }

    @Composable
    private fun FinishButton() {
        val mContext = LocalContext.current
        val prefs = mContext.getSharedPreferences("prefs", Context.MODE_PRIVATE)

        Box(
            contentAlignment = Alignment.BottomEnd, modifier = Modifier.fillMaxSize()
        ) {
            Button(
                onClick = {
                    mContext.startActivity(
                        Intent(
                            mContext, MainActivity::class.java
                        )
                    )
                    // save target to shared preferences
                    prefs.edit().putInt("target", target).apply()
                }, modifier = Modifier.padding(16.dp)
            ) {
                Text("Finish")
            }
        }
    }

    @Composable
    private fun Slider() {
        var sliderValue by remember {
            mutableStateOf(20f)
        }

        Slider(
            value = sliderValue,
            onValueChange = { sliderValue = it.roundToInt().toFloat(); target = it.roundToInt() },
            valueRange = 0f..100f,
            modifier = Modifier.fillMaxWidth().padding(15.dp, 0.dp)
        )
        Text(
            text = sliderValue.roundToInt().toString(),
            Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            fontSize = 35.sp,
        )
    }

}

@Composable
private fun Text() {
    Text(
        text = "Choose a number of words you want to learn everyday",
        fontSize = 22.sp,
        modifier = Modifier
            .fillMaxWidth()
            .padding(0.dp, 250.dp, 0.dp, 0.dp),
        textAlign = TextAlign.Center
    )
}

