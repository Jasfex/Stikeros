package ru.jasfex.stikeros.data

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class StickerPackWithStickers(
    @Embedded
    val stickerPack: StickerPackEntity,
    @Relation(
        parentColumn = "stickerPackUid",
        entityColumn = "stickerUid",
        associateBy = Junction(StickersCrossRef::class)
    )
    val stickers: List<StickerEntity>
)