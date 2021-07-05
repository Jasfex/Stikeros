package ru.jasfex.stikeros.create_sticker

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View


class StickerCanvasView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0,
    defStyleRes: Int = 0
) : View(context, attrs, defStyle, defStyleRes) {

    private val stickerSide = 512
    private var scale = 1.0f

    private var brushSize = 10.0f
    private var brushColor = Color.argb(255, 0, 0, 0)
    private var brushPaint: Paint = createPaint(brushColor, brushSize)

    private var bitmap: Bitmap =
        Bitmap.createBitmap(stickerSide, stickerSide, Bitmap.Config.ARGB_8888)

    private val strokeBorder = 10.0f
    private val strokeSize get() = brushSize + strokeBorder * scale
    private val strokeColor = Color.argb(255, 251, 251, 251)
    private var strokePaint: Paint = createPaint(strokeColor, strokeSize)

    private val shadowSize get() = brushSize
    private val shadowShift = 10.0f
    private val shadowDx = 0.5f
    private val shadowDy = 0.866f
    private val shadowColor = Color.argb(32, 0, 0, 0)
    private var shadowPaint: Paint = createPaint(shadowColor, shadowSize)

    private data class Layer(
        val path: Path,
        val brushPaint: Paint,
        val strokePaint: Paint,
        val shadowPaint: Paint
    )

    private val layers = mutableListOf<Layer>()

    private var currentPath: Path = Path()

    fun onDeleteLastLayer() {
        bitmap = Bitmap.createBitmap(bitmap.width, bitmap.height, Bitmap.Config.ARGB_8888)
        layers.removeLastOrNull()
        invalidate()
    }

    private fun createPaint(paintColor: Int, paintWidth: Float): Paint =
        Paint(Paint.ANTI_ALIAS_FLAG).apply {
            style = Paint.Style.STROKE
            strokeCap = Paint.Cap.ROUND
            strokeJoin = Paint.Join.ROUND
            color = paintColor
            strokeWidth = paintWidth
        }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        scale = w.toFloat() / stickerSide
        brushPaint = createPaint(brushColor, brushSize)
        strokePaint = createPaint(strokeColor, strokeSize)
        shadowPaint = createPaint(shadowColor, shadowSize)
        bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        super.onSizeChanged(w, h, oldw, oldh)
    }

    fun setBrushColor(color: Int) {
        brushColor = color
        brushPaint = createPaint(brushColor, brushSize)
        invalidate()
    }

    fun setBrushSize(size: Float) {
        brushSize = size
        brushPaint = createPaint(brushColor, brushSize)
        strokePaint = createPaint(strokeColor, strokeSize)
        shadowPaint = createPaint(shadowColor, shadowSize)
        invalidate()
    }

    fun generateBitmap(): Bitmap {
        bitmap = Bitmap.createBitmap(bitmap.width, bitmap.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        draw(canvas)
        return Bitmap.createScaledBitmap(bitmap, stickerSide, stickerSide, true)
    }

    override fun onDraw(canvas: Canvas?) {
        drawShadows(canvas)
        drawStrokes(canvas)
        drawContent(canvas)
        invalidate()
    }

    private fun drawShadows(canvas: Canvas?) {
        layers.forEach { layer ->
            val dx = shadowShift * shadowDx * scale
            val dy = shadowShift * shadowDy * scale
            layer.path.offset(dx, dy)
            canvas?.drawPath(layer.path, layer.shadowPaint)
            layer.path.offset(-dx, -dy)
        }
    }

    private fun drawStrokes(canvas: Canvas?) {
        layers.forEach { layer ->
            canvas?.drawPath(layer.path, layer.strokePaint)
        }
    }

    private fun drawContent(canvas: Canvas?) {
        layers.forEach { layer ->
            canvas?.drawPath(layer.path, layer.brushPaint)
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event == null) {
            return super.onTouchEvent(event)
        }
        return when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                currentPath = Path().apply { moveTo(event.x, event.y) }
                layers.add(Layer(currentPath, brushPaint, strokePaint, shadowPaint))
                true
            }
            MotionEvent.ACTION_MOVE -> {
                layers.lastOrNull()?.path?.lineTo(event.x, event.y)
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
}