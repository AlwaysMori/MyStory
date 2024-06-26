package com.nanda.mystory.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.view.View
import android.widget.ProgressBar
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

const val DATE_FORMAT = "ddMMyyyy_HHmmss"
const val MAX_SIZE = 1_000_000

val date: String
    get() = SimpleDateFormat(DATE_FORMAT, Locale.US).format(Date())

fun String.toDateFormat(): String {
    val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US)
    val dateFormatted = inputFormat.parse(this) as Date
    return DateFormat.getDateInstance(DateFormat.FULL).format(dateFormatted)
}

fun createTempFile(context: Context): File {
    val directory = context.externalCacheDir
    return File.createTempFile(date, ".jpg", directory)
}

fun uriToFile(imageUri: Uri, context: Context): File {
    val myFile = createTempFile(context)
    context.contentResolver.openInputStream(imageUri)?.use { inputStream ->
        FileOutputStream(myFile).use { outputStream ->
            val buffer = ByteArray(1024)
            var length: Int
            while (inputStream.read(buffer).also { length = it } > 0) {
                outputStream.write(buffer, 0, length)
            }
        }
    }
    return myFile
}

fun File.reduceFileImage(): File {
    var compressQuality = 100
    val bitmap = BitmapFactory.decodeFile(path)
    var streamLength: Int
    do {
        val bmpStream = ByteArrayOutputStream()
        bitmap?.compress(Bitmap.CompressFormat.JPEG, compressQuality, bmpStream)
        val bmpPicByteArray = bmpStream.toByteArray()
        streamLength = bmpPicByteArray.size
        compressQuality -= 5
    } while (streamLength > MAX_SIZE)
    bitmap?.compress(Bitmap.CompressFormat.JPEG, compressQuality, FileOutputStream(this))
    return this
}

fun isValidEmail(email: String): Boolean {
    return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
}

fun showLoading(isLoading: Boolean, loadingView: ProgressBar) {
    loadingView.visibility = if (isLoading) View.VISIBLE else View.GONE
}
