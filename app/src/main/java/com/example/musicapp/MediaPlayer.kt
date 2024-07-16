package com.example.musicapp

import android.media.MediaPlayer
import android.os.Build
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderColors
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp


@Composable
fun MediaPlayerUI2() {
    val context = LocalContext.current
    val mediaPlayer = remember {
        MediaPlayer.create(context, R.raw.music)
    }

    var isPlaying by remember { mutableStateOf(false) }
    var currentPosition by remember { mutableStateOf(0) }
    var currentPositionSecond by remember { mutableStateOf(0) }
    var duration by remember { mutableStateOf(0) }


    LaunchedEffect(mediaPlayer) {
        duration = mediaPlayer.duration
        val durationInSeconds = duration / 1000
        val minutes = durationInSeconds / 60
        val seconds = durationInSeconds % 60
        while (true) {
            if (isPlaying) {
                currentPosition = mediaPlayer.currentPosition
                currentPositionSecond = currentPosition / 1000
            }
            kotlinx.coroutines.delay(1000L)
        }


    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Duration: ${duration / 1000} seconds")
        Text(text = "Current Position: ${currentPosition / 1000} seconds")

        Button(onClick = {
            if (isPlaying) {
                mediaPlayer.pause()
            } else {
                mediaPlayer.start()
            }
            isPlaying = !isPlaying
        }) {
            Text(text = if (isPlaying) "Pause" else "Play")
        }
    }
}


@Composable
fun MediaPlayerUI3() {
    val context = LocalContext.current
    val mediaPlayer = remember {
        MediaPlayer.create(context, R.raw.music)
    }

    var isPlaying by remember { mutableStateOf(false) }
    var playbackSpeed by remember { mutableStateOf(1.0f) }

    LaunchedEffect(playbackSpeed) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mediaPlayer.playbackParams = mediaPlayer.playbackParams.setSpeed(playbackSpeed)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Playback Speed: $playbackSpeed")
        Button(onClick = {
            if (isPlaying) {
                mediaPlayer.pause()
            } else {
                mediaPlayer.start()
            }
            isPlaying = !isPlaying
        }) {
            Text(text = if (isPlaying) "Pause" else "Play")
        }

        Button(onClick = {
            playbackSpeed = if (playbackSpeed == 1.0f) 0.25f else 1.0f
        }) {
            Text(text = "Toggle Playback Speed")
        }
    }
}


@Preview
@Composable
fun PreviewMediaPlayer() {
    MediaPlayerUI()
}


@Composable
fun MediaPlayerUI() {
    
    val context = LocalContext.current
    val mediaPlayer = remember {
        MediaPlayer.create(context, R.raw.music)
    }

    var isSliderPositions by remember { mutableFloatStateOf(0f) }
    var isPlaying by remember { mutableStateOf(false) }

    var currentPosition by remember { mutableStateOf(0) }

    var duration by remember { mutableStateOf(0) }

    LaunchedEffect(mediaPlayer) {
        duration = mediaPlayer.duration
        while (true) {
            if (isPlaying) {
                currentPosition = mediaPlayer.currentPosition
            }
            kotlinx.coroutines.delay(1000L)
        }
    }

    val durationInSeconds = duration / 1000
    val minutes = durationInSeconds / 60
    val seconds = durationInSeconds % 60


    val currentPositionInSeconds = currentPosition / 1000
    val currentPositionMinutes = currentPositionInSeconds / 60
    val currentPositionSeconds = currentPositionInSeconds % 60

    val formattedTime = String.format("%02d:%02d", currentPositionMinutes, currentPositionSeconds)


    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Image(
            painter = painterResource(id = R.drawable.banner),
            contentDescription = "",
            Modifier
                .fillMaxWidth(0.8f),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.padding(10.dp))

        Text(
            text = "Song Name",
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth(0.8f)

        )
        Slider(
            value = isSliderPositions,
            onValueChange = { newPosition ->
                isSliderPositions = newPosition

                val newPositionInSeconds = (newPosition * durationInSeconds).toInt()
                mediaPlayer.seekTo(newPositionInSeconds * 1000)

                println(newPositionInSeconds)

            },
            modifier = Modifier.fillMaxWidth(0.8f),
            colors = SliderDefaults.colors(
                activeTrackColor = Color.Green,
                inactiveTrackColor = Color.Gray,
                thumbColor = Color.Blue
            ),
            valueRange = 0f..1f
        )
        Row(
            modifier = Modifier
                .fillMaxWidth(0.85f),
            horizontalArrangement = Arrangement.Absolute.SpaceBetween
        ) {
            Text(text = formattedTime)
            Text(text = "$minutes:$seconds")
        }

        Row(
            modifier = Modifier.fillMaxWidth(0.6f),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.back),
                contentDescription = "",
                Modifier.size(30.dp)
                    .clickable {
                        val currentPosition = mediaPlayer.currentPosition
                        val newPosition = (currentPosition - 5 * 1000).coerceAtLeast(0) // 5 saniye geri, 0'dan küçük olamaz
                        mediaPlayer.seekTo(newPosition)
                    }
            )

            Image(
                painter = painterResource(id = if (isPlaying) R.drawable.pause else R.drawable.play),
                contentDescription = "",
                Modifier
                    .size(60.dp)
                    .clickable {
                        if (isPlaying) {
                            mediaPlayer.pause()
                        } else {
                            mediaPlayer.start()
                        }
                        isPlaying = !isPlaying
                    }
            )
            Image(
                painter = painterResource(id = R.drawable.forward),
                contentDescription = "",
                Modifier.size(30.dp)
                    .clickable {
                        val newPosition = currentPosition + 5 * 1000 // 5 saniye ileri
                        mediaPlayer.seekTo(newPosition)

                          println(currentPositionSeconds)


                    }
            )

        }

    }

}