package ru.jasfex.stikeros

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.net.Uri


fun Activity.createAndSaveSticker(): Pair<Bitmap, Uri> {
    val rect = Rect(128, 128, 384, 384)
    val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        .apply {
            color = Color.RED
        }
    val figure = Figure.Rect(rect, paint)

    val layers = listOf<Figure>(figure)

    val stickerBitmap = createSticker(layers)
    val stickerUri = saveSticker("test", stickerBitmap)

    return stickerBitmap to stickerUri
}