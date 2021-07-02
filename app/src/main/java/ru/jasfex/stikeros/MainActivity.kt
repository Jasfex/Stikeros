package ru.jasfex.stikeros

import android.content.ContentValues
import android.content.Intent
import android.graphics.*
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.TypedValue
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.io.FileOutputStream

class MainActivity : AppCompatActivity() {

    private val CREATE_STICKER_PACK_ACTION = "org.telegram.messenger.CREATE_STICKER_PACK"
    private val CREATE_STICKER_PACK_EMOJIS_EXTRA = "STICKER_EMOJIS"
    private val CREATE_STICKER_PACK_IMPORTER_EXTRA = "IMPORTER"

    private sealed class Figure {
        data class Rect(val rect: android.graphics.Rect, val paint: Paint) : Figure()
        data class Path(val path: android.graphics.Path, val paint: Paint) : Figure()
    }

    private fun createSticker(
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

        val strokeWidth = 5.0f
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

    private fun saveSticker(stickerName: String, sticker: Bitmap): Uri {
        val resolver = applicationContext.contentResolver

        val imagesCollection =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                MediaStore.Images.Media.getContentUri(
                    MediaStore.VOLUME_EXTERNAL_PRIMARY
                )
            } else {
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            }

        val stickerDetails = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, stickerName)
            put(MediaStore.Images.Media.MIME_TYPE, "image/png")
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                put(MediaStore.Images.Media.IS_PENDING, 1)
            }
        }

        val stickerContentUri = resolver.insert(imagesCollection, stickerDetails)

        resolver.openFileDescriptor(stickerContentUri!!, "w", null).use { pfd ->
            FileOutputStream(pfd!!.fileDescriptor).use {
                sticker.compress(Bitmap.CompressFormat.PNG, 100, it)
                it.flush()
            }
        }

        stickerDetails.clear()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            stickerDetails.put(MediaStore.Images.Media.IS_PENDING, 0)
        }
        resolver.update(stickerContentUri, stickerDetails, null, null)
        return stickerContentUri
    }

    private fun createAndSaveSticker(): Pair<Bitmap, Uri> {
        val rect = Rect(128, 128, 384, 384)
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
            .apply {
                color = Color.RED
            }
        val figure = Figure.Rect(rect, paint)

        val layers = listOf<Figure>(figure)

        val stickerBitmap = createSticker(layers)
        val stickerUri = saveSticker("test", stickerBitmap)

        return stickerBitmap to stickerUri
    }

    private fun doImport(stickers: ArrayList<Uri>, emojis: ArrayList<String>) {
        val intent = Intent(CREATE_STICKER_PACK_ACTION)
        intent.putExtra(Intent.EXTRA_STREAM, stickers)
        intent.putExtra(CREATE_STICKER_PACK_IMPORTER_EXTRA, packageName)
        intent.putExtra(CREATE_STICKER_PACK_EMOJIS_EXTRA, emojis)
        intent.type = "image/*"
        try {
            startActivity(intent)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val image = findViewById<ImageView>(R.id.image)

        val (stickerBitmap, stickerUri) = createAndSaveSticker()

        image.setImageBitmap(stickerBitmap)

        val stickers = ArrayList<Uri>()
        val emojis = ArrayList<String>()

        stickers.add(stickerUri)
        emojis.add("☺️")

        val myEmojis = ArrayList<String>();
        myEmojis.addAll(
            listOf(
                "\uD83D\uDE02",
                "\uD83D\uDE18",
                "\u2764",
                "\uD83D\uDE0D",
                "\uD83D\uDE0A",
                "\uD83D\uDE01",
                "\uD83D\uDC4D",
                "\u263A",
                "\uD83D\uDE14",
                "\uD83D\uDE04",
                "\uD83D\uDE2D",
                "\uD83D\uDC8B",
                "\uD83D\uDE12",
                "\uD83D\uDE33",
                "\uD83D\uDE1C",
                "\uD83D\uDE48",
                "\uD83D\uDE09",
                "\uD83D\uDE03",
                "\uD83D\uDE22",
                "\uD83D\uDE1D",
                "\uD83D\uDE31",
                "\uD83D\uDE21",
                "\uD83D\uDE0F",
                "\uD83D\uDE1E",
                "\uD83D\uDE05",
                "\uD83D\uDE1A",
                "\uD83D\uDE4A",
                "\uD83D\uDE0C",
                "\uD83D\uDE00",
                "\uD83D\uDE0B",
                "\uD83D\uDE06",
                "\uD83D\uDC4C",
                "\uD83D\uDE10",
                "\uD83D\uDE15"
            )
        )
//
//        myEmojis.add("☺️");
//        myEmojis.add("\uD83D\uDE22");
//        myEmojis.add("\uD83E\uDD73");
//        myEmojis.add("\uD83E\uDD2A");
//        myEmojis.add("\uD83D\uDE18️");
//        myEmojis.add("\uD83D\uDE18️");
//        myEmojis.add("\uD83E\uDD2A");
//        myEmojis.add("\uD83E\uDD73");
//        myEmojis.add("☺️");
        findViewById<TextView>(R.id.text).apply {
            text = myEmojis.joinToString(" ")
            setTextSize(TypedValue.COMPLEX_UNIT_SP, 24.0f)
        }

        // doImport(stickers, emojis)
    }

}