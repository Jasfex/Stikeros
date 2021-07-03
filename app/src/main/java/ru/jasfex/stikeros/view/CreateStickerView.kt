package ru.jasfex.stikeros.view

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.annotation.ColorInt
import kotlin.math.min

class CreateStickerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0,
    defStyleRes: Int = 0
) : View(context, attrs, defStyle, defStyleRes) {

    // TODO("SCALE SIZE WITH RESPECT TO 512/WIDTH")
    val shadowSize = 10.0f
    val shadowDx = 5.0f
    val shadowDy = 8.66f
    val shadowColor = Color.argb(32, 0, 0, 0)

    val shadowPaint = createPaint(shadowColor, shadowSize)

    // TODO("SCALE SIZE WITH RESPECT TO 512/WIDTH")
    var strokePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeCap = Paint.Cap.ROUND
        color = Color.argb(255, 251, 251, 251)
        strokeWidth = 5.0f * 4
    }

    private sealed class Shape {
        class Path(val path: android.graphics.Path) : Shape()
    }

    private class Layer(
        val shape: Shape,
        val paint: Paint,
        val strokePaint: Paint
    )

    private fun createPaint(@ColorInt paintColor: Int, radius: Float): Paint {
        return Paint().apply {
            isAntiAlias = true
            style = Paint.Style.STROKE
            color = paintColor
            strokeWidth = radius
            strokeCap = Paint.Cap.ROUND
            strokeJoin = Paint.Join.ROUND
        }
    }

    private var paint = createPaint(Color.BLACK, 10.0f)
    private val layers = mutableListOf<Layer>()

    fun setPaintColor(color: Int) {
        paint = createPaint(color, paint.strokeWidth)
        invalidate()
    }

    fun setPaintSize(size: Int) {
        paint = createPaint(paint.color, size.toFloat())

        strokePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            style = Paint.Style.STROKE
            strokeCap = Paint.Cap.ROUND
            color = Color.argb(255, 0, 251, 0)
            strokeWidth = size + 10.0f
        }
        invalidate()
    }

    private var path = android.graphics.Path()

    fun popLayer() {
        layers.removeLastOrNull()
        invalidate()
        println("SALAM layers.size = ${layers.size}")
    }

    // TODO("RESIZE STICKER TO 512x512")
    fun getBitmap(): Bitmap {
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        draw(canvas)
        return bitmap
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        return when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                path = android.graphics.Path()
                path.moveTo(event.x, event.y)
                layers.add(Layer(Shape.Path(path), paint, strokePaint))
                true
            }
            MotionEvent.ACTION_MOVE -> {
                layers.lastOrNull()?.let {
                    (it.shape as? Shape.Path)?.let {
                        it.path.lineTo(event.x, event.y)
                    }
                }
                invalidate()
                true
            }
            MotionEvent.ACTION_UP -> {
                invalidate()
                true
            }
            else -> super.onTouchEvent(event)
        }
    }

    override fun onDraw(canvas: Canvas) {
        drawShadows(canvas) // TODO("fix shadows")
        drawStrokes(canvas)
        drawLayers(canvas)
        invalidate()
    }

    private fun drawShadows(canvas: Canvas) {
        layers.forEach { layer: Layer ->
            when (layer.shape) {
                is Shape.Path -> {
                    val path = layer.shape.path
                    path.offset(shadowDx, shadowDy)
                    canvas.drawPath(layer.shape.path, shadowPaint)
                    path.offset(-shadowDx, -shadowDy)
                }
            }
        }
    }

    private fun drawStrokes(canvas: Canvas) {
        layers.forEach { layer: Layer ->
            when (layer.shape) {
                is Shape.Path -> {
                    canvas.drawPath(layer.shape.path, layer.strokePaint)
                }
            }
        }
    }

    private fun drawLayers(canvas: Canvas) {
        layers.forEach { layer: Layer ->
            when (layer.shape) {
                is Shape.Path -> {
                    canvas.drawPath(layer.shape.path, layer.paint)
                }
            }
        }
    }

}

