package ru.jasfex.stikeros.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import ru.jasfex.stikeros.data.entity.Sticker
import ru.jasfex.stikeros.data.entity.Stickerpack
import ru.jasfex.stikeros.data.entity.StickerpackStickerCrossRef

@Database(
    entities = [Sticker::class, Stickerpack::class, StickerpackStickerCrossRef::class],
    version = 1,
    exportSchema = false
)
abstract class StickerosDatabase : RoomDatabase() {

    abstract fun stickerosDao(): StickerosDao

    companion object {
        @Volatile
        private var INSTANCE: StickerosDatabase? = null

        fun getInstance(context: Context): StickerosDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    StickerosDatabase::class.java,
                    "stickeros.db"
                ).build().also {
                    INSTANCE = it
                }
            }
        }
    }
}