package top.nabil.nugazlah.screen

import android.media.MediaPlayer
import android.provider.Settings
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import top.nabil.nugazlah.R
import top.nabil.nugazlah.alarm.AlarmData
import top.nabil.nugazlah.ui.theme.WhitePlain

@Composable
fun AlarmScreen(
    data: AlarmData,
    navController: NavController
) {
    val context = LocalContext.current
    var isPlaying by remember { mutableStateOf(true) }
    var mediaPlayer: MediaPlayer? by remember { mutableStateOf(null) }

    LaunchedEffect(Unit) {
        mediaPlayer = MediaPlayer.create(context, R.raw.alarm_sound).apply {
            isLooping = true
            start()
        }
    }

    LaunchedEffect(isPlaying) {
        if (!isPlaying) {
            mediaPlayer?.stop()
            mediaPlayer?.release()
            mediaPlayer = null
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(32.dp, top = 48.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = data.title,
            color = WhitePlain,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.headlineSmall,
            fontSize = 20.sp,
            maxLines = 2,
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            modifier = Modifier.fillMaxWidth(),
            text = data.deadline,
            color = WhitePlain,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.headlineSmall,
            fontSize = 20.sp,
            maxLines = 2,
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            modifier = Modifier.fillMaxWidth(),
            text = data.description,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.bodyMedium.copy(
                fontSize = 16.sp,
                lineHeight = 16.sp,
                letterSpacing = 0.1.sp
            )
        )

        Spacer(modifier = Modifier.height(80.dp))

        Button(
            onClick = {
                isPlaying = false
                navController.navigate("${Screen.DetailTaskScreen}/${data.taskId}") {
                    popUpTo(Screen.AlarmScreen) { inclusive = true }
                    launchSingleTop = true
                    restoreState = true
                }
            },
            shape = CircleShape,
            colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
            modifier = Modifier.size(150.dp)
        ) {
            Text("Stop Alarm", color = Color.White)
        }
    }
}
