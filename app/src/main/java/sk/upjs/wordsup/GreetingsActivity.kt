@file:OptIn(ExperimentalUnitApi::class)

package sk.upjs.wordsup

import android.content.Intent
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.*
import sk.upjs.wordsup.ui.theme.WordsUpTheme

class GreetingsActivity : ComponentActivity() {

    private var nameGlobal = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val name = Prefs.getInstance(applicationContext).name
        val target = Prefs.getInstance(applicationContext).target
        if (name != "" && target == 0){
            applicationContext.startActivity(
                Intent(
                    this, TargetActivity::class.java
                ).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            )
            finish()
        }
        if(name != "" && target != 0){
            applicationContext.startActivity(
                Intent(
                    this, MainActivity::class.java
                ).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            )
            finish()
        }

        setContent {
            GreetingsActivity()
        }
    }

    @Composable
    private fun GreetingsActivity() {

        WordsUpTheme {
            Surface(
                modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
            ) {
                Column {
                    Text()
                    TextField()
                    NextButton()
                }
            }
        }
    }

    @Composable
    private fun NextButton() {
        val mContext = LocalContext.current
        Box(
            contentAlignment = Alignment.BottomEnd, modifier = Modifier.fillMaxSize()
        ) {
            Button(
                onClick = {
                    mContext.startActivity(
                        Intent(
                            mContext, TargetActivity::class.java
                        )
                    )
                    // save name to shared preferences
                    Prefs.getInstance(mContext).name = nameGlobal
                }, modifier = Modifier.padding(16.dp)
            ) {
                Text("Next")
            }
        }
    }

    @Composable
    private fun TextField() {
        var name by remember { mutableStateOf("") }
        TextField(
            value = name,
            onValueChange = { name = it; nameGlobal = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(25.dp, 20.dp)
                .background(color = Color.White),
            textStyle = TextStyle(
                fontSize = 35.sp
            ),
            singleLine = true
        )
    }

    @Composable
    private fun Text() {
        Text(
            text = "Enter your name:",
            fontSize = 35.sp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(0.dp, 250.dp, 0.dp, 0.dp),
            textAlign = TextAlign.Center
        )
    }
}
