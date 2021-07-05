package ru.jasfex.stikeros.domain

import android.app.Activity
import android.content.Intent
import android.net.Uri

fun Activity.shareStickers(stickers: ArrayList<Uri>, emojis: ArrayList<String>) {
    val CREATE_STICKER_PACK_ACTION = "org.telegram.messenger.CREATE_STICKER_PACK"
    val CREATE_STICKER_PACK_EMOJIS_EXTRA = "STICKER_EMOJIS"
    val CREATE_STICKER_PACK_IMPORTER_EXTRA = "IMPORTER"

    val intent = Intent(CREATE_STICKER_PACK_ACTION)
    intent.putExtra(Intent.EXTRA_STREAM, stickers)
    intent.putExtra(CREATE_STICKER_PACK_IMPORTER_EXTRA, packageName)
    intent.putExtra(CREATE_STICKER_PACK_EMOJIS_EXTRA, emojis)
    intent.type = "image/*"
    println("SALAM $intent $stickers $emojis $packageName")
    try {
        startActivity(intent)
    } catch (e: Exception) {
        e.printStackTrace()
    }
}
