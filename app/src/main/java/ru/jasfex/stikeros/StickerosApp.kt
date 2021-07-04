package ru.jasfex.stikeros

import android.app.Application
import ru.jasfex.stikeros.data.StickerosDao
import ru.jasfex.stikeros.data.StickerosDatabase

class StickerosApp : Application() {

    lateinit var  stickerosDao: StickerosDao

    override fun onCreate() {
        super.onCreate()
        val database = StickerosDatabase.getInstance(this)
        stickerosDao = database.stickerosDao()
    }
}