import android.content.ContentValues
import android.content.Context
import android.media.MediaRecorder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.annotation.RequiresApi
import java.io.File
import java.io.FileDescriptor
import java.io.IOException

class AudioRecorder(
    private val context: Context,
) {

    private var recorder: MediaRecorder? = null
    private var outputFile: String? = null
    private var fileDescriptor: FileDescriptor? = null

    fun startRecording() {
        val resolver = context.contentResolver

        val fileName = "record_${System.currentTimeMillis()}.m4a"

        val values = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
            put(MediaStore.MediaColumns.MIME_TYPE, "audio/mp4")
            put(MediaStore.MediaColumns.RELATIVE_PATH, "Download")
        }

        val uri = resolver.insert(
            MediaStore.Downloads.EXTERNAL_CONTENT_URI,
            values
        ) ?: return


        outputFile = "Download/" + fileName

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

    fun stopRecording(): String? {
        recorder?.apply {
            stop()
            release()
        }
        recorder = null
        return outputFile
    }
}