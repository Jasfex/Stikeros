package ru.jasfex.stikeros

import android.app.Application
import ru.jasfex.stikeros.data.StickerosDao
import ru.jasfex.stikeros.data.StickerosDatabase

class StickerosApp : Application() {

    val stickerosDao: StickerosDao by lazy {
        val database = StickerosDatabase.getInstance(this)
        database.stickerosDao()
    }

}