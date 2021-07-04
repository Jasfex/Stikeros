package ru.jasfex.stikeros.data

import androidx.room.*
import ru.jasfex.stikeros.data.entity.Sticker
import ru.jasfex.stikeros.data.entity.Stickerpack
import ru.jasfex.stikeros.data.entity.relation.StickerpackWithStickers

@Dao
interface StickerosDao {

    @Query("SELECT * FROM stickers WHERE uid = :uid")
    fun getSticker(uid: Long): Sticker

    @Query("SELECT * FROM stickers")
    fun getStickers(): List<Sticker>

    @Insert(entity = Sticker::class)
    suspend fun saveSticker(sticker: Sticker)

    @Delete(entity = Sticker::class)
    suspend fun deleteSticker(sticker: Sticker)

    @Query("DELETE FROM stickers WHERE uid = :uid")
    suspend fun deleteSticker(uid: Long)


    @Query("SELECT * FROM stickerpacks WHERE uid = :uid")
    fun getStickerpack(uid: Long): Stickerpack

    @Query("SELECT * FROM stickerpacks")
    fun getStickerpacks(): List<Stickerpack>

    @Insert(entity = Stickerpack::class)
    suspend fun saveStickerpack(stickerpack: Stickerpack)

    @Delete(entity = Stickerpack::class)
    suspend fun deleteStickerpack(stickerpack: Stickerpack)

    @Query("DELETE FROM stickerpacks WHERE uid = :uid")
    suspend fun deleteStickerpack(uid: Long)


    @Transaction
    @Query("SELECT * FROM stickerpacks")
    fun getStickerpackWithStickers(): List<StickerpackWithStickers>

}