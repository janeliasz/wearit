package com.example.wearit.data

import android.graphics.Bitmap
import java.io.ByteArrayOutputStream

class FakeInternalStorageHelper : IInternalStorageHelper {
    val fakePhotos = mutableMapOf<String, ByteArray>(
        "item-1.png" to ByteArray(100),
        "item-2.png" to ByteArray(100),
        "item-3.png" to ByteArray(100)
    )

    override fun savePhoto(bitmap: Bitmap): String {
        val name = "item-" + (fakePhotos.size + 1) + ".png"
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
        fakePhotos[name] = stream.toByteArray()
        return name
    }

    override fun loadPhotos(): Map<String, ByteArray> {
        return fakePhotos
    }

    override fun deleteFile(filename: String): Boolean {
        return fakePhotos.remove(filename) != null
    }
}