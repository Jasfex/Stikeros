package ru.jasfex.stikeros.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE

@Entity(
    tableName = "sticker_stickerpack_cross_ref",
    primaryKeys = ["stickerpackUid", "stickerUid"],
    foreignKeys = [
        ForeignKey(
            entity = Stickerpack::class,
            parentColumns = ["stickerpackUid"],
            childColumns = ["stickerpackUid"],
            onDelete = CASCADE
        ),
        ForeignKey(
            entity = Sticker::class,
            parentColumns = ["stickerUid"],
            childColumns = ["stickerUid"],
            onDelete = CASCADE
        )
    ]
)
data class StickerpackStickerCrossRef(
    @ColumnInfo(name = "stickerpackUid") val stickerpackUid: Long,
    @ColumnInfo(name = "stickerUid") val stickerUid: Long
)