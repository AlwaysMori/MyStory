package com.nanda.mystory.utils

import android.graphics.Bitmap
import android.widget.ProgressBar
import java.text.DateFormat
import java.util.Date
import android.graphics.BitmapFactory
import java.io.ByteArrayOutputStream
import android.view.View
import java.io.InputStream
import android.content.Context
import java.io.File
import java.text.SimpleDateFormat
import java.util.Locale
import android.net.Uri
import java.io.FileOutputStream



const val DATE_FORMAT = "ddMMyyyy_HHmmss"
val date: String = SimpleDateFormat(DATE_FORMAT, Locale.US).format(Date())

const val MAX_SIZE = 1_000_000



fun String.toDateFormat(): String {
    val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US)
    val dateFormatted = inputFormat.parse(this) ?: throw IllegalArgumentException("Invalid date format")
    return DateFormat.getDateInstance(DateFormat.FULL).format(dateFormatted)
}



fun temptFile(context: Context): File {
    val directory = context.externalCacheDir
    val dateFormatted = SimpleDateFormat(DATE_FORMAT, Locale.US).format(Date())
    return File.createTempFile(dateFormatted, ".jpg", directory)
}


fun uriToFile(imageUri: Uri, context: Context): File {
    val myFile = temptFile(context)
    val inputStream = context.contentResolver.openInputStream(imageUri) ?: throw IllegalArgumentException("Unable to open input stream")
    val outputStream = FileOutputStream(myFile)

    val buffer = ByteArray(1024)
    var length: Int
    while (inputStream.read(buffer).also { length = it } > 0) {
        outputStream.write(buffer, 0, length)
    }

    outputStream.close()
    inputStream.close()

    return myFile
}

fun File.reduceFileImage(): File {
    val bitmap = BitmapFactory.decodeFile(this.path) ?: throw IllegalArgumentException("Invalid file path or unable to decode bitmap")
    var compressQuality = 100
    var streamLength: Int

    do {
        val bmpStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, compressQuality, bmpStream)
        val bmpPicByteArray = bmpStream.toByteArray()
        streamLength = bmpPicByteArray.size
        compressQuality -= 5
    } while (streamLength > MAX_SIZE && compressQuality > 0)

    FileOutputStream(this).use { outputStream ->
        bitmap.compress(Bitmap.CompressFormat.JPEG, compressQuality, outputStream)
    }

    return this
}


fun isValidEmail(email: String): Boolean {
    return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
}

fun showLoading(isLoading: Boolean, loadingView: ProgressBar) {
    loadingView.visibility = if (isLoading) View.VISIBLE else View.GONE
}


