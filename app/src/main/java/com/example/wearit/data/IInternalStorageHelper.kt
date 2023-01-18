package com.example.wearit.data

import android.graphics.Bitmap

interface IInternalStorageHelper {
    fun savePhoto(bitmap: Bitmap): String
    fun loadPhotos(): Map<String, ByteArray>
    fun deleteFile(filename: String): Boolean
}