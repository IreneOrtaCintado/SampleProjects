package ioc.mediaplayer

import android.content.Context
import android.database.Cursor
import android.os.Environment
import android.provider.MediaStore
import java.io.File

fun listInternalFiles(path: String = "Download") {
    var testFolder: File =
        File(Environment.getExternalStorageDirectory().getPath() + File.separator + path)
    println("IS EXISTS: " + testFolder.exists())
    println("IS DIRECTORY: " + testFolder.isDirectory)
    println("IS READABLE: " + testFolder.canRead())
    println("IS WRITABLE: " + testFolder.canWrite())
    println("IS CONTENT: " + testFolder.extension)

    if (!testFolder.list().isNullOrEmpty())
        for (i in testFolder.list())
            println(i)
}

fun listSdCardFiles(path: String = "Download") {
    var testFolder: File =
        File(File.separator+"sdcard" + File.separator + path)
    println("IS EXISTS: " + testFolder.exists())
    println("IS DIRECTORY: " + testFolder.isDirectory)
    println("IS READABLE: " + testFolder.canRead())
    println("IS WRITABLE: " + testFolder.canWrite())
    println("IS CONTENT: " + testFolder.extension)

    if (!testFolder.list().isNullOrEmpty())
        for (i in testFolder.list())
            println(i)
}

fun testFiles(context: Context) {

    var testFolder: File =
        File(Environment.getExternalStorageDirectory().getPath() + "/Download")
    println("IS EXISTS: " + testFolder.exists())
    println("IS DIRECTORY: " + testFolder.isDirectory)
    println("IS READABLE: " + testFolder.canRead())
    println("IS WRITABLE: " + testFolder.canWrite())
    println("IS CONTENT: " + testFolder.extension)

    if (!testFolder.parentFile.list().isNullOrEmpty())
        for (i in testFolder.parentFile.list())
            println(i)

    var testFolder2: File = context.filesDir
    println("IS EXISTS: " + testFolder2.exists())
    println("IS DIRECTORY: " + testFolder2.isDirectory)
    println("IS READABLE: " + testFolder2.canRead())
    println("IS WRITABLE: " + testFolder2.canWrite())
    println(testFolder2.absolutePath)

    if (!testFolder2.parentFile.list().isNullOrEmpty())
        for (i in testFolder2.parentFile.list())
            println(i)

    var folders = context.externalMediaDirs
    if (!folders.isNullOrEmpty())
        for (i in folders)
            println(i.name + ": " + i.absolutePath)

    var audioURI = MediaStore.Audio.Media.INTERNAL_CONTENT_URI
    println(audioURI.path)
    println(MediaStore.Audio.Media.TITLE)
    println(MediaStore.Audio.Media.ARTIST)

    val proyection = arrayOf(
        MediaStore.Audio.Media.TITLE,  //title
        MediaStore.Audio.Media.DURATION,  // duracion
        MediaStore.Audio.Media.DATA,  //path
        MediaStore.Audio.Media.ARTIST
    )

    val audioCursor: Cursor? =
        context.contentResolver.query(audioURI, proyection, null, null, null)

    if (audioCursor != null) {
        while (audioCursor.moveToNext()) {
            val title = audioCursor.getString(0)
            val duration = audioCursor.getString(1)
            val path = audioCursor.getString(2)
            val artist = audioCursor.getString(3)
            println(title + duration + path + artist)
        }
    }
}