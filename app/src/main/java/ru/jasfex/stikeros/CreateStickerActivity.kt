package ru.jasfex.stikeros

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import ru.jasfex.stikeros.domain.saveSticker
import ru.jasfex.stikeros.domain.shareStickers
import ru.jasfex.stikeros.view.ColorPickerView
import ru.jasfex.stikeros.view.CreateStickerView
import ru.jasfex.stikeros.view.SizePickerView

class CreateStickerActivity : AppCompatActivity() {

    private val viewModel = CreateStickerViewModel()

    private lateinit var stickerView: CreateStickerView
    private lateinit var selectedColor: View
    private lateinit var colorPicker: ColorPickerView
    private lateinit var sizePicker: SizePickerView
    private lateinit var btnUndo: Button
    private lateinit var btnShare: Button

    private fun initViews() {
        stickerView = findViewById(R.id.sticker)
        selectedColor = findViewById(R.id.selected_color)
        colorPicker = findViewById(R.id.color_picker)
        sizePicker = findViewById(R.id.size_picker)
        btnUndo = findViewById(R.id.btn_undo)
        btnShare = findViewById(R.id.btn_share)

        configureBtnUndo()
        configureBtnShare()
        configureColorPicker(stickerView::setPaintColor, sizePicker::setColor, selectedColor)
        configureSizePicker(stickerView::setPaintSize)
    }

    private fun configureBtnUndo() {
        btnUndo.setOnClickListener {
            stickerView.popLayer()
        }
    }

    private fun configureBtnShare() {
        btnShare.setOnClickListener {
            val stickerBitmap = stickerView.getBitmap()
            val stickerUri = saveSticker(stickerBitmap, viewModel::onSaveSticker)

            val stickers = ArrayList<Uri>()
            val emojis = ArrayList<String>()

            stickers.add(stickerUri)
            emojis.add(Emojis[0])

            shareStickers(stickers, emojis)
        }
    }

    private fun configureColorPicker(
        setPaintColor: (color: Int) -> Unit,
        setSizePaintColor: (color: Int) -> Unit,
        selectedColor: View
    ) {
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
        colorPicker.setOnColorPicked {
            setPaintColor(it)
            setSizePaintColor(it)
            selectedColor.setBackgroundColor(it)
        }
    }

    private fun configureSizePicker(
        setPaintSize: (size: Int) -> Unit
    ) {
        sizePicker.setOnSizePicked {
            setPaintSize(it)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_sticker)

        initViews()
    }
}