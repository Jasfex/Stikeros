package ru.jasfex.stikeros

import android.graphics.Paint

sealed class Figure {
    data class Rect(val rect: android.graphics.Rect, val paint: Paint) : Figure()
    data class Path(val path: android.graphics.Path, val paint: Paint) : Figure()
}