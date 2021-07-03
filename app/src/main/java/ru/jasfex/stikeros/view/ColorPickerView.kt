package ru.jasfex.stikeros.view

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.graphics.get

class ColorPickerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0,
) : AppCompatImageView(context, attrs, defStyle) {

    private var bitmap = Bitmap.createBitmap(512, 1, Bitmap.Config.ARGB_8888)
    private var canvas = Canvas(bitmap)
    private var onColorPicked: (color: Int) -> Unit = {}

    fun setOnColorPicked(func: (color: Int) -> Unit) {
        onColorPicked = func
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        try {
            bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
            canvas = Canvas(bitmap)
        } catch (th: Throwable) {

        }
    }

    private fun pickColor(x: Float, y: Float): Int {
        return try {
            draw(canvas)
            val color = bitmap[x.toInt(), y.toInt()]
            onColorPicked(color)
            color
        } catch (th: Throwable) {
            Color.BLACK
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        return when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                pickColor(event.x, event.y)
                true
            }
            MotionEvent.ACTION_MOVE -> {
                pickColor(event.x, event.y)
                true
            }
            MotionEvent.ACTION_UP -> {
                pickColor(event.x, event.y)
                true
            }
            else -> super.onTouchEvent(event)
        }
    }
}