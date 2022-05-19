package ioc.mediaplayer.multimedia

import java.io.File
import java.util.concurrent.TimeUnit

data class AudioFile(
    var uriIdIO: Long,
    var titleIO: String?,
    var artistIO: String?,
    var durationIO: Long,
    var yearIO: String?
) {
    fun getDurationTime():String{
        return millisecondsToShortTime(durationIO)
    }
}

/**
 * Transforms the track duration in milliseconds to a string in mm:ss format
 */
fun millisecondsToShortTime(millisecondsIO: Long): String {
    return String.format(
        "%02d:%02d",
        TimeUnit.MILLISECONDS.toMinutes(millisecondsIO) -
                TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millisecondsIO)),
        TimeUnit.MILLISECONDS.toSeconds(millisecondsIO) -
                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisecondsIO))
    )
}

fun millisecondsToTime(milliseconds: Long): String {
    return String.format(
        "%02d:%02d:%02d",
        TimeUnit.MILLISECONDS.toHours(milliseconds),
        TimeUnit.MILLISECONDS.toMinutes(milliseconds) -
                TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(milliseconds)),
        TimeUnit.MILLISECONDS.toSeconds(milliseconds) -
                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(milliseconds))
    )
}

fun supportedAudioFormat(file: File): Boolean {
    return file.exists() &&
            file.isFile &&
            file.canRead() &&
            (file.endsWith(".wav") ||
                    file.endsWith(".flac") ||
                    file.endsWith(".acc") ||
                    file.endsWith(".3gp") ||
                    file.endsWith(".amr") ||
                    file.endsWith(".ogg") ||
                    file.endsWith(".mp3")
                    )
}
