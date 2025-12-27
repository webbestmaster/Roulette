import android.content.ContentValues
import android.content.Context
import android.media.*
import android.os.Build
import android.provider.MediaStore
import android.net.Uri
import kotlinx.coroutines.*
import java.io.FileOutputStream

class VoiceAudioRecorder(private val context: Context) {
    private var audioRecord: AudioRecord? = null
    private var recordingJob: Job? = null
    private val sampleRate = 44100
    private val channelConfig = AudioFormat.CHANNEL_IN_MONO
    private val audioFormat = AudioFormat.ENCODING_PCM_16BIT

    fun startRecording() {
        val bufferSize = AudioRecord.getMinBufferSize(sampleRate, channelConfig, audioFormat)
        audioRecord = AudioRecord(
            MediaRecorder.AudioSource.VOICE_RECOGNITION,
            sampleRate,
            channelConfig,
            audioFormat,
            bufferSize
        )

        // Создаём файл через MediaStore
        val resolver = context.contentResolver
        val fileName = "record_${System.currentTimeMillis()}.wav"
        val values = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
            put(MediaStore.MediaColumns.MIME_TYPE, "audio/wav")
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                put(MediaStore.MediaColumns.RELATIVE_PATH, "Download")
            }
        }

        val outputUri = resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, values)
        val pfd = resolver.openFileDescriptor(outputUri!!, "w") ?: return
        val outputStream = FileOutputStream(pfd.fileDescriptor)

        // Записываем WAV-заголовок (пока с нулевой длиной данных)
        writeWavHeader(outputStream, sampleRate, 1, 16, 0)

        audioRecord!!.startRecording()

        recordingJob = CoroutineScope(Dispatchers.IO).launch {
            val buffer = ByteArray(bufferSize)
            var totalAudioLen = 0L

            while (isActive) {
                val read = audioRecord!!.read(buffer, 0, buffer.size)
                if (read > 0) {
                    outputStream.write(buffer, 0, read)
                    totalAudioLen += read
                }
            }

            // После остановки записи — обновляем WAV-заголовок с реальной длиной
            updateWavHeader(outputStream, totalAudioLen)
            outputStream.close()
        }
    }

    fun stopRecording() {
        recordingJob?.cancel()
        recordingJob = null

        audioRecord?.apply {
            stop()
            release()
        }
        audioRecord = null
    }

//    fun getOutputUri(): Uri? = outputUri

    /** Записываем заголовок WAV */
    private fun writeWavHeader(
        out: FileOutputStream,
        sampleRate: Int,
        channels: Int,
        bitsPerSample: Int,
        totalAudioLen: Long,
    ) {
        val byteRate = sampleRate * channels * bitsPerSample / 8
        val header = ByteArray(44)

        // RIFF chunk descriptor
        header[0] = 'R'.code.toByte()
        header[1] = 'I'.code.toByte()
        header[2] = 'F'.code.toByte()
        header[3] = 'F'.code.toByte()
        writeInt(header, 4, (36 + totalAudioLen).toInt())
        header[8] = 'W'.code.toByte()
        header[9] = 'A'.code.toByte()
        header[10] = 'V'.code.toByte()
        header[11] = 'E'.code.toByte()

        // fmt subchunk
        header[12] = 'f'.code.toByte()
        header[13] = 'm'.code.toByte()
        header[14] = 't'.code.toByte()
        header[15] = ' '.code.toByte()
        writeInt(header, 16, 16) // Subchunk1Size
        writeShort(header, 20, 1.toShort()) // AudioFormat PCM = 1
        writeShort(header, 22, channels.toShort())
        writeInt(header, 24, sampleRate)
        writeInt(header, 28, byteRate)
        writeShort(header, 32, (channels * bitsPerSample / 8).toShort())
        writeShort(header, 34, bitsPerSample.toShort())

        // data subchunk
        header[36] = 'd'.code.toByte()
        header[37] = 'a'.code.toByte()
        header[38] = 't'.code.toByte()
        header[39] = 'a'.code.toByte()
        writeInt(header, 40, totalAudioLen.toInt())

        out.write(header, 0, 44)
    }

    /** Обновляем длину аудио после записи */
    private fun updateWavHeader(out: FileOutputStream, totalAudioLen: Long) {
//        val byteRate = sampleRate * 1 * 16 / 8
        val header = ByteArray(8)
        writeInt(header, 0, (36 + totalAudioLen).toInt()) // RIFF size
        writeInt(header, 4, totalAudioLen.toInt()) // data chunk size

        out.channel.position(4)
        out.write(header, 0, 4)
        out.channel.position(40)
        out.write(header, 4, 4)
    }

    private fun writeInt(header: ByteArray, offset: Int, value: Int) {
        header[offset] = (value and 0xff).toByte()
        header[offset + 1] = ((value shr 8) and 0xff).toByte()
        header[offset + 2] = ((value shr 16) and 0xff).toByte()
        header[offset + 3] = ((value shr 24) and 0xff).toByte()
    }

    private fun writeShort(header: ByteArray, offset: Int, value: Short) {
        header[offset] = (value.toInt() and 0xff).toByte()
        header[offset + 1] = ((value.toInt() shr 8) and 0xff).toByte()
    }
}
