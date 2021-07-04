package ru.jasfex.stikeros.domain

import android.app.Activity
import ru.jasfex.stikeros.StickerosApp
import ru.jasfex.stikeros.data.StickerosDao

fun Activity.getStickerosDao(): StickerosDao {
    return (application as StickerosApp).stickerosDao
}