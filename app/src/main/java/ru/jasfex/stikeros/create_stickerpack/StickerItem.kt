package ru.jasfex.stikeros.create_stickerpack

import ru.jasfex.stikeros.data.entity.Sticker

data class StickerItem(
    val sticker: Sticker,
    val selected: Boolean
)