package ru.jasfex.stikeros.create_sticker

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.appcompat.widget.AppCompatImageView
import kotlin.math.max
import kotlin.math.min

class SizePickerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0,
) : AppCompatImageView(context, attrs, defStyle) {

    private val minSize = 1
    private val maxSize = 128
    private var size = (maxSize + minSize) / 2.0f

    private var onSizePicked: (size: Float) -> Unit = {}

    fun setSize(proposedSize: Float) {
        size = min(maxSize.toFloat(), max(minSize.toFloat(), proposedSize))
        invalidate()
    }

    fun setOnSizePickedListener(callback: (size: Float) -> Unit) {
        onSizePicked = callback
    }

    private var pointerColor: Int = Color.BLACK
    private var pointerPaint = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.FILL
        color = pointerColor
        strokeCap = Paint.Cap.ROUND
        strokeJoin = Paint.Join.ROUND
    }

    fun setPointerColor(color: Int) {
        pointerColor = color
        pointerPaint.color = pointerColor
        invalidate()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        val radius = min(width, height).toFloat()
        if (radius > 0) {
            pointerPaint.strokeWidth = radius
            val cx = (size - minSize.toFloat()) / (maxSize - minSize).toFloat() * width
            val cy = height / 2.0f
            canvas?.drawCircle(cx, cy, size / 2.0f, pointerPaint)
        }
    }

    private fun pickSize(x: Float, y: Float) {
        val scale = max(0.0f, x) / max(1.0f, width.toFloat())
        val k = max(1.0f, (maxSize - minSize).toFloat())
        val b = minSize.toFloat()
        size = min(maxSize.toFloat(), max(minSize.toFloat(), k * scale + b))
        onSizePicked(size)
        invalidate()
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event == null) {
            return super.onTouchEvent(event)
        }
        return when (event.action) {
            MotionEvent.ACTION_DOWN, MotionEvent.ACTION_MOVE, MotionEvent.ACTION_UP -> {
                pickSize(event.x, event.y)
                true
            }
            else -> super.onTouchEvent(event)
        }
    }
}