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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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

    @Preview(
        showSystemUi = true,
        device = "spec:width=411dp,height=891dp,dpi=420,isRound=false,chinSize=0dp,orientation=landscape"
    )
    @Composable
    private fun MainContent() {
        WordsUpTheme {
            Surface(
                modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
            ) {
                Text()
            }
        }
    }

    @Composable
    private fun Text() {
        Box(
            contentAlignment = Alignment.Center, // you apply alignment to all children
            modifier = Modifier.fillMaxSize()
        ) {

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Choose a number of words you want to learn everyday",
                    fontSize = 22.sp,
                    textAlign = TextAlign.Center
                )

                var sliderValue by remember {
                    mutableStateOf(20f)
                }


                Slider(
                    value = sliderValue,
                    onValueChange = {
                        sliderValue = it.roundToInt().toFloat();
                        target = it.roundToInt()
                    },
                    valueRange = 0f..100f,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(15.dp, 0.dp)
                )
                Text(
                    text = sliderValue.roundToInt().toString(),
                    Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    fontSize = 35.sp,
                )
            }
            var flag by remember { mutableStateOf(false) }
            if (flag) {
                // save target to shared preferences
                Prefs.getInstance(LocalContext.current).target = target
                openMain(LocalContext.current)
            }

            Box(
                modifier = Modifier.align(Alignment.BottomEnd)
            ) {
                Button(
                    onClick = {
                        flag = true
                    },
                    modifier = Modifier.padding(16.dp),
                ) {
                    Text("Finish")
                }
            }

        }

    }

    fun openMain(context: Context) {
        context.startActivity(
            Intent(
                context, MainActivity::class.java
            )
        )
    }

}
