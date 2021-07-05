package ru.jasfex.stikeros.create_sticker_pack

import ru.jasfex.stikeros.data.StickerEntity

data class StickerItem(
    val sticker: StickerEntity,
    val selected: Boolean
)