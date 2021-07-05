package ru.jasfex.stikeros

import android.app.Application
import ru.jasfex.stikeros.data.StickerAppDao
import ru.jasfex.stikeros.data.StickerAppDatabase

class StickerApp : Application() {
    private val database: StickerAppDatabase by lazy { StickerAppDatabase.getInstance(this) }
    val stickerAppDao: StickerAppDao by lazy { database.stickerAppDao() }
}