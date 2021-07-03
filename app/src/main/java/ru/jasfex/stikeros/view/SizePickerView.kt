package ru.jasfex.stikeros.view

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

    private val sizePaint = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.FILL
        color = Color.argb(255, 0, 0, 0)
        strokeCap = Paint.Cap.ROUND
        strokeJoin = Paint.Join.ROUND
    }

    private val minSize = 1
    private val maxSize = 128

    private var currentSize = 10

    private var onSizePicked: (size: Int) -> Unit = {}

    fun setOnSizePicked(func: (size: Int) -> Unit) {
        onSizePicked = func
    }

    fun setColor(color: Int) {
        sizePaint.color = color
        invalidate()
    }

    private fun handlePickSize(x: Int, width: Int): Boolean {
        val scale = max(0, x).toFloat() / max(1, width).toFloat()
        val k = max(1, maxSize - minSize)
        val b = minSize
        val size = min(maxSize, max(minSize, (k * scale + b).toInt()))
        currentSize = size
        onSizePicked(size)
        invalidate()
        return true
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        val radius = min(width, height).toFloat()
        if (radius > 0) {
            sizePaint.strokeWidth = radius.toFloat()
            val cx = (currentSize.toFloat() - minSize) / (maxSize.toFloat() - minSize) * width.toFloat()
            canvas?.drawCircle(cx, height.toFloat() / 2.0f, currentSize.toFloat() / 2.0f, sizePaint)
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        return when (event.action) {
            MotionEvent.ACTION_DOWN, MotionEvent.ACTION_MOVE, MotionEvent.ACTION_UP -> handlePickSize(
                event.x.toInt(),
                width
            )
            else -> super.onTouchEvent(event)
        }
    }
}