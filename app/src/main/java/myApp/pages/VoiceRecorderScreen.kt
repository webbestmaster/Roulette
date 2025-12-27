import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.io.File

@Composable
fun VoiceRecorderScreen() {
    val context = LocalContext.current
    val recorder = remember { AudioRecorder(context) }

    var isRecording by remember { mutableStateOf(false) }
    var recordedFile by remember { mutableStateOf<File?>(null) }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Button(
            onClick = {
                if (!isRecording) {
                    recorder.startRecording()
                } else {
                    recordedFile = recorder.stopRecording()
                }
                isRecording = !isRecording
            }
        ) {
            Text(
                text = if (isRecording) "Остановить запись" else "Начать запись"
            )
        }

        recordedFile?.let {
            Spacer(modifier = Modifier.height(16.dp))
            Text("Файл сохранён:")
            Text(it.absolutePath, fontSize = 12.sp)
        }
    }
}
