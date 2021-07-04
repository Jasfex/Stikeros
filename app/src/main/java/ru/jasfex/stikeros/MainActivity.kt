package ru.jasfex.stikeros

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import ru.jasfex.stikeros.create_sticker.CreateStickerActivity
import ru.jasfex.stikeros.create_stickerpack.CreateStickerPackActivity
import ru.jasfex.stikeros.share_stickerpack.ShareStickerpackActivity


class MainActivity : AppCompatActivity() {

    private lateinit var btnCreateSticker: Button
    private lateinit var btnCreateStickerpack: Button
    private lateinit var btnShareStickerpack: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnCreateSticker = findViewById(R.id.btn_create_sticker)
        btnCreateStickerpack = findViewById(R.id.btn_create_stickerpack)
        btnShareStickerpack = findViewById(R.id.btn_share_stickerpack)

        btnCreateSticker.bindDestination(CreateStickerActivity::class.java)
        btnCreateStickerpack.bindDestination(CreateStickerPackActivity::class.java)
        btnShareStickerpack.bindDestination(ShareStickerpackActivity::class.java)
    }

    private fun <T> Button.bindDestination(clazz: Class<T>) {
        setOnClickListener {
            startActivity(Intent(this.context, clazz))
        }
    }

    // TODO("Generate random face")
    // TODO("Build release with keystore")
    // TODO("Upload APK")
    // TODO("Update repository README.md")
}