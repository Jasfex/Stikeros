package ru.jasfex.stikeros.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "stickerpacks")
data class Stickerpack(
    @PrimaryKey(autoGenerate = true) val uid: Long = 0L,
    val name: String
)