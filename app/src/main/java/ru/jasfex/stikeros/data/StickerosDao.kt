package ru.jasfex.stikeros.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import ru.jasfex.stikeros.data.entity.Sticker
import ru.jasfex.stikeros.data.entity.Stickerpack
import ru.jasfex.stikeros.data.entity.StickerpackStickerCrossRef
import ru.jasfex.stikeros.data.entity.relation.StickerpackWithStickers

@Dao
interface StickerosDao {

    @Query("SELECT * FROM stickers WHERE stickerUid = :uid")
    fun getSticker(uid: Long): Sticker

    @Query("SELECT * FROM stickers")
    fun getStickers(): List<Sticker>

    @Query("SELECT * FROM stickers")
    fun getStickersFlow(): Flow<List<Sticker>>

    @Insert(entity = Sticker::class)
    suspend fun saveSticker(sticker: Sticker)

    @Delete(entity = Sticker::class)
    suspend fun deleteSticker(sticker: Sticker)

    @Query("DELETE FROM stickers WHERE stickerUid = :uid")
    suspend fun deleteSticker(uid: Long)


    @Query("SELECT * FROM stickerpacks WHERE stickerpackUid = :uid")
    fun getStickerpack(uid: Long): Stickerpack

    @Query("SELECT * FROM stickerpacks")
    fun getStickerpacks(): List<Stickerpack>

    @Insert(entity = Stickerpack::class)
    suspend fun saveStickerpack(stickerpack: Stickerpack): Long

    @Delete(entity = Stickerpack::class)
    suspend fun deleteStickerpack(stickerpack: Stickerpack)

    @Query("DELETE FROM stickerpacks WHERE stickerpackUid = :uid")
    suspend fun deleteStickerpack(uid: Long)


    @Query("SELECT * FROM sticker_stickerpack_cross_ref")
    fun getCrossRefs(): List<StickerpackStickerCrossRef>

    @Insert(entity = StickerpackStickerCrossRef::class)
    fun saveCrossRefs(crossRef: StickerpackStickerCrossRef)

    @Delete(entity = StickerpackStickerCrossRef::class)
    fun deleteCrossRefs(crossRef: StickerpackStickerCrossRef)


    @Transaction
    @Query("SELECT * FROM stickerpacks")
    fun getStickerpackWithStickers(): List<StickerpackWithStickers>

}