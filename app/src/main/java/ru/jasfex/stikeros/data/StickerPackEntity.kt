package ru.jasfex.stikeros.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "sticker_packs")
data class StickerPackEntity(
    @PrimaryKey(autoGenerate = true) val stickerPackUid: Long = 0L,
    val name: String
)