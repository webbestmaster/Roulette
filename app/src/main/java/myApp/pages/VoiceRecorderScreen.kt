import android.net.Uri
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
    val recorder = remember { VoiceAudioRecorder(context) }

    var isRecording by remember { mutableStateOf(false) }
    var recordedFile by remember { mutableStateOf<String?>(null) }

    Column(
//        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Button(
            onClick = {
                if (isRecording) {
                    recorder.stopRecording()
                } else {
                    recorder.startRecording()
                }
                isRecording = !isRecording
            }
        ) {
            Text(
                text = if (isRecording) "Остановить запись" else "Начать запись"
            )
        }

/*
        recordedFile?.let {
            Spacer(modifier = Modifier.height(16.dp))
            Text("Файл сохранён:")
            Text(it.toString(), fontSize = 12.sp)
        }
*/
    }
}
