package ru.jasfex.stikeros.create_sticker

import android.content.ContentValues
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.jasfex.stikeros.R
import ru.jasfex.stikeros.StickerApp
import ru.jasfex.stikeros.data.StickerEntity
import java.io.FileOutputStream

class CreateStickerActivity : AppCompatActivity() {

    private val initialBrushColor = Color.BLUE
    private val initialBrushSize = 20.0f

    private val app: StickerApp get() = application as StickerApp

    private val ioScope = CoroutineScope(Dispatchers.IO)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_sticker)

        val stickerCanvas = findViewById<StickerCanvasView>(R.id.sticker_canvas)!!
        val brushColor = findViewById<View>(R.id.brush_color)!!
        val colorPicker = findViewById<ColorPickerView>(R.id.color_picker)!!
        val sizePicker = findViewById<SizePickerView>(R.id.size_picker)!!
        val emoji = findViewById<TextView>(R.id.tv_emoji)!!
        val btnPrevEmoji = findViewById<ImageButton>(R.id.btn_prev_emoji)!!
        val btnNextEmoji = findViewById<ImageButton>(R.id.btn_next_emoji)!!
        val btnUndo = findViewById<Button>(R.id.btn_undo)!!
        val btnSaveSticker = findViewById<Button>(R.id.btn_save_sticker)!!

        setupStickerCanvas(stickerCanvas)
        setupBrushColor(brushColor)
        setupColorPicker(colorPicker)
        setupSizePicker(sizePicker)
        setupEmoji(emoji, btnPrevEmoji, btnNextEmoji)

        colorPicker.setOnColorPickedListener { color ->
            stickerCanvas.setBrushColor(color)
            brushColor.setBackgroundColor(color)
            sizePicker.setPointerColor(color)
        }

        sizePicker.setOnSizePickedListener { size ->
            stickerCanvas.setBrushSize(size)
        }

        btnUndo.setOnClickListener {
            stickerCanvas.onDeleteLastLayer()
        }

        btnSaveSticker.setOnClickListener {
            val stickerBitmap = stickerCanvas.generateBitmap()
            val stickerEmoji = emoji.text.toString()
            saveSticker(stickerBitmap, stickerEmoji) { sUri, sEmoji ->
                ioScope.launch {
                    val stickerEntity = StickerEntity(uri = sUri, emoji = sEmoji)
                    app.stickerAppDao.saveSticker(stickerEntity)
                }
            }
        }
    }

    private fun setupStickerCanvas(stickerCanvas: StickerCanvasView) {
        stickerCanvas.setBrushColor(initialBrushColor)
        stickerCanvas.setBrushSize(initialBrushSize)
    }

    private fun setupSizePicker(sizePicker: SizePickerView) {
        sizePicker.setPointerColor(initialBrushColor)
        sizePicker.setSize(initialBrushSize)
    }

    private fun setupBrushColor(brushColor: View) {
        brushColor.setBackgroundColor(initialBrushColor)
    }

    private fun setupColorPicker(colorPicker: ColorPickerView) {
        val colors = intArrayOf(
            Color.BLACK,
            Color.RED,
            Color.YELLOW,
            Color.GREEN,
            Color.BLUE,
            Color.MAGENTA,
            Color.CYAN,
            Color.WHITE
        )

        val gradient = GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, colors).apply {
            shape = GradientDrawable.RECTANGLE
            gradientType = GradientDrawable.LINEAR_GRADIENT
        }

        colorPicker.setImageDrawable(gradient)
    }

    private fun setupEmoji(emoji: TextView, btnPrevEmoji: ImageButton, btnNextEmoji: ImageButton) {
        emoji.text = Emojis[0]
        btnPrevEmoji.setOnClickListener {
            val index = Emojis.indexOf(emoji.text.toString())
            emoji.text = if (index < 1) {
                Emojis.last()
            } else {
                Emojis[index - 1]
            }
        }
        btnNextEmoji.setOnClickListener {
            val index = Emojis.indexOf(emoji.text.toString())
            emoji.text = if (index < 0) {
                Emojis.last()
            } else {
                Emojis[(index + 1) % Emojis.size]
            }
        }
    }

    private fun saveSticker(
        stickerBitmap: Bitmap,
        stickerEmoji: String,
        saveInDatabase: (stickerUri: String, stickerEmoji: String) -> Unit
    ) {
        val resolver = applicationContext.contentResolver

        val imagesCollection =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                MediaStore.Images.Media.getContentUri(
                    MediaStore.VOLUME_EXTERNAL_PRIMARY
                )
            } else {
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            }

        val filename = "StickerApp_" + System.currentTimeMillis().toString()
        val imageDetails = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, filename)
            put(MediaStore.Images.Media.MIME_TYPE, "image/png")
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                put(MediaStore.Images.Media.IS_PENDING, 1)
            }
        }

        val contentUri = resolver.insert(imagesCollection, imageDetails)!!

        resolver.openFileDescriptor(contentUri, "w", null).use { pfd ->
            FileOutputStream(pfd!!.fileDescriptor).use { fos ->
                stickerBitmap.compress(Bitmap.CompressFormat.PNG, 100, fos)
                fos.flush()
            }
        }

        imageDetails.clear()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            imageDetails.put(MediaStore.Images.Media.IS_PENDING, 0)
            resolver.update(contentUri, imageDetails, null, null)
        }

        saveInDatabase(contentUri.toString(), stickerEmoji)
        Toast.makeText(this, "Sticker \"$contentUri\" saved!", Toast.LENGTH_SHORT).show()
    }

}