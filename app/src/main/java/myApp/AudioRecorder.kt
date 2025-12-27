import android.content.ContentValues
import android.content.Context
import android.media.MediaRecorder
import android.os.Build
import android.provider.MediaStore
import androidx.annotation.RequiresApi
import java.io.File
import java.io.FileDescriptor
import java.io.IOException

class AudioRecorder(
    private val context: Context
) {

    private var recorder: MediaRecorder? = null
    private var outputFile: File? = null

//    private var recorder: MediaRecorder? = null
    private var fileDescriptor: FileDescriptor? = null

    @RequiresApi(Build.VERSION_CODES.Q)
    fun startRecording() {
        val resolver = context.contentResolver

        val fileName = "record_${System.currentTimeMillis()}.m4a"

        val values = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
            put(MediaStore.MediaColumns.MIME_TYPE, "audio/mp4")

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                put(
                    MediaStore.MediaColumns.RELATIVE_PATH,
                    "Download"
                )
            }
        }

        val uri = resolver.insert(
            MediaStore.Downloads.EXTERNAL_CONTENT_URI,
            values
        ) ?: return

        val pfd = resolver.openFileDescriptor(uri, "w") ?: return
        fileDescriptor = pfd.fileDescriptor

        recorder = MediaRecorder().apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
            setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
            setOutputFile(fileDescriptor)

            prepare()
            start()
        }
    }

    fun stopRecording(): File? {
        recorder?.apply {
            stop()
            release()
        }
        recorder = null
        return outputFile
    }
}