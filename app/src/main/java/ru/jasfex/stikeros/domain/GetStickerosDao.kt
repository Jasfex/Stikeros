package ru.jasfex.stikeros.domain

import android.app.Activity
import ru.jasfex.stikeros.StickerApp
import ru.jasfex.stikeros.data.StickerosDao

fun Activity.getStickerosDao(): StickerosDao {
    return (application as StickerApp).stickerDao
}