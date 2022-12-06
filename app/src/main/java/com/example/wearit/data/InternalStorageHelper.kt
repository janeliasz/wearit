package com.example.wearit.data

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.IOException
import java.util.UUID

class InternalStorageHelper(private val context: Context) {
    fun savePhoto(bitmap: Bitmap): String {
        return try {
            val filename = UUID.randomUUID().toString() + ".png"
            context.openFileOutput(filename, Context.MODE_PRIVATE).use { stream ->
                bitmap.setHasAlpha(true)
                if(!bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)) {
                    throw IOException("Could not save photo.")
                }
            }
            filename
        } catch(e: IOException) {
            e.printStackTrace()
            ""
        }
    }

    fun loadPhotos(): Map<String, Bitmap> {
        val photosMap: MutableMap<String, Bitmap> = mutableMapOf()

        val files = context.filesDir.listFiles()
        files.filter { it.canRead() && it.isFile && it.name.endsWith(".png")}.forEach() {
            val bytes = it.readBytes()
            val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
            photosMap[it.name] = bitmap
        }

        return photosMap
    }

    fun deleteFile(filename: String): Boolean {
        return try {
            context.deleteFile(filename)
        } catch (e: IOException) {
            e.printStackTrace()
            false
        }
    }
}