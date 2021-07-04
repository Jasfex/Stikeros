package ru.jasfex.stikeros.domain

import android.app.Activity
import android.content.ContentValues
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import ru.jasfex.stikeros.data.StickerosDao
import java.io.FileOutputStream

fun Activity.saveSticker(
    sticker: Bitmap,
    saveStickerInDatabase: (dao: StickerosDao, uri: Uri) -> Unit
): Uri {
    val resolver = applicationContext.contentResolver

    val imagesCollection =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            MediaStore.Images.Media.getContentUri(
                MediaStore.VOLUME_EXTERNAL_PRIMARY
            )
        } else {
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        }

    val filename = "Strikeros_${System.currentTimeMillis()}"
    val stickerDetails = ContentValues().apply {
        put(MediaStore.Images.Media.DISPLAY_NAME, filename)
        put(MediaStore.Images.Media.MIME_TYPE, "image/png")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            put(MediaStore.Images.Media.IS_PENDING, 1)
        }
    }

    val stickerContentUri = resolver.insert(imagesCollection, stickerDetails)

    resolver.openFileDescriptor(stickerContentUri!!, "w", null).use { pfd ->
        FileOutputStream(pfd!!.fileDescriptor).use {
            sticker.compress(Bitmap.CompressFormat.PNG, 100, it)
            it.flush()
        }
    }

    stickerDetails.clear()
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        stickerDetails.put(MediaStore.Images.Media.IS_PENDING, 0)
    }
    resolver.update(stickerContentUri, stickerDetails, null, null)

    saveStickerInDatabase(getStickerosDao(), stickerContentUri)

    return stickerContentUri
}