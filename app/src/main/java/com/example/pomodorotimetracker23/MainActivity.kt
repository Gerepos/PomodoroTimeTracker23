package com.example.pomodorotimetracker23

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.pomodorotimetracker23.ui.theme.PomodoroTimeTracker23Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val pomodoroTimer = PomodoroTimer(
            workDuration = 25 * 60 * 1000L,
            shortBreakDuration = 5 * 60 * 1000L,
            longBreakDuration = 15 * 60 * 1000L,
            onTick = {},
            onSessionEnd = {}
        )
        setContent {
            PomodoroScreen(timer = pomodoroTimer)
        }
    }
}

@Composable
fun PomodoroScreen(
    modifier: Modifier = Modifier,
    timer: PomodoroTimer
) {
    var timeRemaining by remember { mutableStateOf(0L) }
    var isWorking by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        timer.startWorkSession()
    }

    PomodoroTimeTracker23Theme {
        Scaffold(
            modifier = modifier.fillMaxSize(),
            topBar = {
                Text(
                    text = if (isWorking) "Work Session" else "Break Session",
                    modifier = Modifier.padding(16.dp)
                )
            },
            content = { innerPadding ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .padding(16.dp)
                ) {
                    Text(
                        text = formatTime(timeRemaining),
                        style = MaterialTheme.typography.h3,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                    Spacer(modifier = Modifier.height(32.dp))
                    Row(
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    ) {
                        Button(onClick = {
                            if (isWorking) {
                                timer.stop()
                                isWorking = false
                                timer.startShortBreak()
                            } else {
                                timer.stop()
                                isWorking = true
                                timer.startWorkSession()
                            }
                        }) {
                            Text(text = if (isWorking) "Start Break" else "Start Work")
                        }
                    }
                }
            }
        )
    }

    timer.apply {
        onTick = { time ->
            timeRemaining = time
        }
        onSessionEnd = {
            isWorking = !isWorking
            if (isWorking) {
                startWorkSession()
            } else {
                startShortBreak()
            }
        }
    }
}

fun formatTime(timeMillis: Long): String {
    val minutes = (timeMillis / 1000) / 60
    val seconds = (timeMillis / 1000) % 60
    return String.format("%02d:%02d", minutes, seconds)
}
/*@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    PomodoroTimeTracker23Theme {
        Greeting("Android")
    }
}
 */