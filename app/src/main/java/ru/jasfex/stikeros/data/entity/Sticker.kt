package ru.jasfex.stikeros.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "stickers")
data class Sticker(
    @PrimaryKey(autoGenerate = true) val uid: Long = 0L,
    val uri: String,
    val emoji: String
)