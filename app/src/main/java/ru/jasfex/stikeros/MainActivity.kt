package ru.jasfex.stikeros

import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {

    private lateinit var image: ImageView
    private lateinit var text: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        image = findViewById(R.id.image)
        text = findViewById(R.id.text)

        val (stickerBitmap, stickerUri) = createAndSaveSticker()

        image.setImageBitmap(stickerBitmap)
        text.text = Emojis.joinToString(" ")

        val stickers = ArrayList<Uri>()
        val emojis = ArrayList<String>()

        stickers.add(stickerUri)
        emojis.add("☺️")

        shareStickers(stickers, emojis)
    }

}