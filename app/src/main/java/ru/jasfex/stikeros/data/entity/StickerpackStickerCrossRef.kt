package ru.jasfex.stikeros.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE

@Entity(
    tableName = "sticker_stickerpack_cross_ref",
    primaryKeys = ["s_uid", "sp_uid"],
    foreignKeys = [
        ForeignKey(
            entity = Stickerpack::class,
            parentColumns = ["uid"],
            childColumns = ["sp_uid"],
            onDelete = CASCADE
        ),
        ForeignKey(
            entity = Sticker::class,
            parentColumns = ["uid"],
            childColumns = ["s_uid"],
            onDelete = CASCADE
        )
    ]
)
data class StickerpackStickerCrossRef(
    @ColumnInfo(name = "sp_uid") val stickerpackUid: Long,
    @ColumnInfo(name = "s_uid") val stickerUid: Long
)