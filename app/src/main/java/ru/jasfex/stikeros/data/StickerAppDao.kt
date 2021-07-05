package ru.jasfex.stikeros.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface StickerAppDao {

    @Query("SELECT * FROM stickers")
    fun getStickers(): List<StickerEntity>

    @Query("SELECT * FROM stickers")
    fun getStickersFlow(): Flow<List<StickerEntity>>

    @Insert(entity = StickerEntity::class)
    suspend fun saveSticker(sticker: StickerEntity): Long

    @Query("DELETE FROM stickers WHERE stickerUid = :stickerUid")
    suspend fun deleteSticker(stickerUid: Long)


    @Query("SELECT * FROM sticker_packs")
    fun getStickerPacks(): List<StickerPackEntity>

    @Query("SELECT * FROM sticker_packs")
    fun getStickerPacksFlow(): Flow<List<StickerPackEntity>>

    @Insert(entity = StickerPackEntity::class)
    suspend fun saveStickerPack(stickerPack: StickerPackEntity): Long

    @Query("DELETE FROM sticker_packs WHERE stickerPackUid = :stickerPackUid")
    suspend fun deleteStickerPack(stickerPackUid: Long)


    @Query("SELECT * FROM stickers_cross_refs")
    fun getStickersCrossRefs(): List<StickersCrossRef>

    @Query("SELECT * FROM stickers_cross_refs")
    fun getStickersCrossRefsFlow(): Flow<List<StickersCrossRef>>

    @Insert(entity = StickersCrossRef::class)
    suspend fun saveStickersCrossRef(crossRef: StickersCrossRef)

    @Delete(entity = StickersCrossRef::class)
    suspend fun deleteStickersCrossRef(crossRef: StickersCrossRef)


    @Transaction
    @Query("SELECT * FROM sticker_packs")
    fun getStickerPacksWithStickers(): Flow<List<StickerPackWithStickers>>

}