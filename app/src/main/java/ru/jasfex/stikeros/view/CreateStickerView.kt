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

class CreateStickerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0,
    defStyleRes: Int = 0
) : View(context, attrs, defStyle, defStyleRes) {

    val STICKER_SIDE_SIZE = 512

    var SCALE = 1.0f

    var PAINT_SIZE = 10.0f

    val SHADOW_SIZE get() = PAINT_SIZE
    val SHADOW_SHIFT = 10.0f
    val SHADOW_DX = 0.5f
    val SHADOW_DY = 0.866f
    val SHADOW_COLOR = Color.argb(32, 0, 0, 0)
    var SHADOW_PAINT: Paint? = null

    val STROKE_BORDER_SIZE = 10.0f
    val STROKE_SIZE get() = PAINT_SIZE + STROKE_BORDER_SIZE * SCALE
    val STROKE_COLOR = Color.argb(255, 251, 251, 251)
    var STROKE_PAINT: Paint? = null

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        if (w > 0) {
            SCALE = w.toFloat() / STICKER_SIDE_SIZE.toFloat()
            SHADOW_PAINT = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                style = Paint.Style.STROKE
                strokeCap = Paint.Cap.ROUND
                strokeJoin = Paint.Join.ROUND
                color = SHADOW_COLOR
                strokeWidth = SHADOW_SIZE
            }
            STROKE_PAINT = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                style = Paint.Style.STROKE
                strokeCap = Paint.Cap.ROUND
                strokeJoin = Paint.Join.ROUND
                color = STROKE_COLOR
                strokeWidth = STROKE_SIZE
            }
        }
        super.onSizeChanged(w, h, oldw, oldh)
    }

    private sealed class Shape {
        class Path(val path: android.graphics.Path) : Shape()
    }

    private class Layer(
        val shape: Shape,
        val paint: Paint,
        val strokePaint: Paint,
        val shadowPaint: Paint
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

    private var paint = createPaint(Color.BLACK, PAINT_SIZE)
    private val layers = mutableListOf<Layer>()

    fun setPaintColor(color: Int) {
        paint = createPaint(color, paint.strokeWidth)
        invalidate()
    }

    fun setPaintSize(size: Int) {
        PAINT_SIZE = size.toFloat()
        paint = createPaint(paint.color, size.toFloat())

        SHADOW_PAINT = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            style = Paint.Style.STROKE
            strokeCap = Paint.Cap.ROUND
            strokeJoin = Paint.Join.ROUND
            color = SHADOW_COLOR
            strokeWidth = SHADOW_SIZE
        }
        STROKE_PAINT = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            style = Paint.Style.STROKE
            strokeCap = Paint.Cap.ROUND
            strokeJoin = Paint.Join.ROUND
            color = STROKE_COLOR
            strokeWidth = STROKE_SIZE
        }
        invalidate()
    }

    private var path = android.graphics.Path()

    fun popLayer() {
        layers.removeLastOrNull()
        invalidate()
    }

    fun getBitmap(): Bitmap {
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        draw(canvas)
        return Bitmap.createScaledBitmap(bitmap, STICKER_SIDE_SIZE, STICKER_SIDE_SIZE, true)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        return when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                path = android.graphics.Path()
                path.moveTo(event.x, event.y)
                layers.add(Layer(Shape.Path(path), paint, STROKE_PAINT!!, SHADOW_PAINT!!))
                true
            }
            MotionEvent.ACTION_MOVE -> {
                layers.lastOrNull()?.let {
                    (it.shape as? Shape.Path)?.path?.lineTo(event.x, event.y)
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
        drawShadows(canvas)
        drawStrokes(canvas)
        drawLayers(canvas)
        invalidate()
    }

    private fun drawShadows(canvas: Canvas) {
        layers.forEach { layer: Layer ->
            when (layer.shape) {
                is Shape.Path -> {
                    val path = layer.shape.path
                    val dx = SHADOW_SHIFT * SHADOW_DX * SCALE
                    val dy = SHADOW_SHIFT * SHADOW_DY * SCALE
                    path.offset(dx, dy)
                    canvas.drawPath(layer.shape.path, layer.shadowPaint)
                    path.offset(-dx, -dy)
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