package ru.jasfex.stikeros.data

import androidx.room.Entity

@Entity(
    tableName = "stickers_cross_refs",
    primaryKeys = ["stickerPackUid", "stickerUid"]
)
data class StickersCrossRef(
    val stickerPackUid: Long,
    val stickerUid: Long
)