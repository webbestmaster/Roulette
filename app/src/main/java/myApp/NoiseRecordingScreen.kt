package myApp

import VoiceAudioRecorder
import NoiseWatcher
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext

@Composable
fun NoiseRecordingScreen() {
    val context = LocalContext.current

    // Создаём recorder один раз и сохраняем между recompositions
    val recorder = remember { VoiceAudioRecorder(context) }

    // Создаём watcher и запускаем его один раз
    val watcher = remember {
        NoiseWatcher(
            startRecording = { recorder.startRecording() },
            stopRecording = { recorder.stopRecording() },
            thresholdDb = 50.0
        )
    }

    // Запуск watcher при появлении экрана
    LaunchedEffect(Unit) {
        watcher.startWatching()
    }

    // Остановка watcher при уходе с экрана
    DisposableEffect(Unit) {
        onDispose { watcher.stopWatching() }
    }

    // UI (кнопка для теста, можно убрать)
    Column (
//        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Автоматическая запись по уровню шума")
    }
}
