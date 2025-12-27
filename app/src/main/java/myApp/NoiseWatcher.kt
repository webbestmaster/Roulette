import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import android.util.Log
import kotlinx.coroutines.*
import kotlin.math.log10
import kotlin.math.sqrt

class NoiseWatcher(
    private val startRecording: () -> Unit,
    private val stopRecording: () -> Unit,
    private val sampleRate: Int = 44100,
    private val thresholdDb: Double = 50.0 // уровень шума в дБ
) {
    private var watcherJob: Job? = null
    private var audioRecord: AudioRecord? = null
    private val bufferSize = AudioRecord.getMinBufferSize(
        sampleRate,
        AudioFormat.CHANNEL_IN_MONO,
        AudioFormat.ENCODING_PCM_16BIT
    )

    fun startWatching() {
        audioRecord = AudioRecord(
            MediaRecorder.AudioSource.MIC,
            sampleRate,
            AudioFormat.CHANNEL_IN_MONO,
            AudioFormat.ENCODING_PCM_16BIT,
            bufferSize
        )

        audioRecord?.startRecording()

        watcherJob = CoroutineScope(Dispatchers.IO).launch {
            val buffer = ShortArray(bufferSize)
            var recording = false
            var silentTime = 0L

            while (isActive) {
                val read = audioRecord?.read(buffer, 0, buffer.size) ?: 0

                if (read > 0) {
                    val rms = calculateRMS(buffer, read)
                    val db = 20 * log10(rms)

                    if (db > thresholdDb) {
                        Log.d("!!!", "noise >>>>")
                        // шум выше порога
                        silentTime = 0L
                        if (!recording) {
                            startRecording()
                            recording = true
                        }
                    } else {
                        Log.d("!!!", "noise <<<<<")

                        // шум ниже порога
                        silentTime += 100 // предполагаем, что цикл ~100ms
                        if (recording && silentTime >= 5_000) { // 30 секунд молчания
                            stopRecording()
                            recording = false
                            silentTime = 0L
                        }
                    }

                    delay(100) // проверяем каждые 100ms
                }
            }
        }
    }

    fun stopWatching() {
        watcherJob?.cancel()
        watcherJob = null
        audioRecord?.apply {
            stop()
            release()
        }
        audioRecord = null
    }

    private fun calculateRMS(buffer: ShortArray, read: Int): Double {
        var sum = 0.0
        for (i in 0 until read) {
            sum += buffer[i] * buffer[i]
        }
        return sqrt(sum / read)
    }
}
