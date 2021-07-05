package ru.jasfex.stikeros

import android.app.Application
import ru.jasfex.stikeros.data.StickerosDao
import ru.jasfex.stikeros.data.StickerAppDatabase

class StickerApp : Application() {
    private val database: StickerAppDatabase by lazy { StickerAppDatabase.getInstance(this) }
    val stickerDao: StickerosDao by lazy { database.stickerDao() }
}