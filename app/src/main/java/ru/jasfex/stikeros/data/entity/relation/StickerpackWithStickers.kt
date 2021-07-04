package ru.jasfex.stikeros.data.entity.relation

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import ru.jasfex.stikeros.data.entity.Sticker
import ru.jasfex.stikeros.data.entity.Stickerpack
import ru.jasfex.stikeros.data.entity.StickerpackStickerCrossRef

data class StickerpackWithStickers(
    @Embedded val stickerpack: Stickerpack,
    @Relation(
        parentColumn = "uid",
        entityColumn = "uid",
        associateBy = Junction(StickerpackStickerCrossRef::class)
    )
    val stickers: List<Sticker>
)