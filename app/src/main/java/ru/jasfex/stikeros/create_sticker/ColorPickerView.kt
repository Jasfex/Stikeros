package ru.jasfex.stikeros.create_sticker

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

    fun setOnColorPickedListener(callback: (color: Int) -> Unit) {
        onColorPicked = callback
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        canvas = Canvas(bitmap)
        super.onSizeChanged(w, h, oldw, oldh)
    }

    private fun pickColor(x: Float, y: Float) {
        try {
            draw(canvas)
            onColorPicked(bitmap[x.toInt(), y.toInt()])
        } catch (th: Throwable) {
            onColorPicked(Color.BLACK)
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event == null) {
            return super.onTouchEvent(event)
        }
        return when (event.action) {
            MotionEvent.ACTION_DOWN, MotionEvent.ACTION_MOVE, MotionEvent.ACTION_UP -> {
                pickColor(event.x, event.y)
                true
            }
            else -> super.onTouchEvent(event)
        }
    }
}