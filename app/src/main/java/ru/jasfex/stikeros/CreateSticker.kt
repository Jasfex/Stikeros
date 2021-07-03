package ru.jasfex.stikeros

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint

fun Activity.createSticker(
    layers: List<Figure>
): Bitmap {
    val bitmap = Bitmap.createBitmap(512, 512, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)

    val shadowSize = 10.0f
    val shadowDx = 2.5f
    val shadowDy = 4.33f
    val shadowColor = Color.argb(64, 0, 0, 0)

    val shadowPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    shadowPaint.color = Color.TRANSPARENT
    shadowPaint.setShadowLayer(shadowSize, shadowDx, shadowDy, shadowColor)

    val strokeWidth = 5.0f * 2
    val strokeColor = Color.argb(255, 251, 251, 251)

    val strokePaint = Paint(Paint.ANTI_ALIAS_FLAG)
    strokePaint.style = Paint.Style.STROKE
    strokePaint.color = strokeColor
    strokePaint.strokeWidth = strokeWidth

    for (layer in layers) {
        when (layer) {
            is Figure.Rect -> {
                val (rect, paint) = layer
                canvas.drawRect(rect, shadowPaint)
                canvas.drawRect(rect, strokePaint)
                canvas.drawRect(rect, paint)
            }
            is Figure.Path -> {
                val (path, paint) = layer
                canvas.drawPath(path, shadowPaint)
                canvas.drawPath(path, strokePaint)
                canvas.drawPath(path, paint)
            }
        }
    }

    return bitmap
}