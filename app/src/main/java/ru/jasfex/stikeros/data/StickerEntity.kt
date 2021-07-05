package ru.jasfex.stikeros.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "stickers")
data class StickerEntity(
    @PrimaryKey(autoGenerate = true) val stickerUid: Long = 0L,
    val uri: String,
    val emoji: String
)