package com.example.wearit.data

import android.content.Context
import android.graphics.Bitmap
import java.io.IOException
import java.util.UUID

class InternalStorageHelper(private val context: Context) : IInternalStorageHelper {
    override fun savePhoto(bitmap: Bitmap): String {
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

    fun savePhoto(bitmap: Bitmap, filename: String): String {
        return try {
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

    override fun loadPhotos(): Map<String, ByteArray> {
        val photosMap: MutableMap<String, ByteArray> = mutableMapOf()

        val files = context.filesDir.listFiles()
        files.filter { it.canRead() && it.isFile && it.name.endsWith(".png")}.forEach() {
            val bytes = it.readBytes()
            photosMap[it.name] = bytes
        }

        return photosMap
    }

    override fun deleteFile(filename: String): Boolean {
        return try {
            context.deleteFile(filename)
        } catch (e: IOException) {
            e.printStackTrace()
            false
        }
    }
}