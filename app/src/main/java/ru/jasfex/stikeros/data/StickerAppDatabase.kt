package ru.jasfex.stikeros.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [StickerEntity::class, StickerPackEntity::class, StickersCrossRef::class],
    version = 1,
    exportSchema = false
)
abstract class StickerAppDatabase : RoomDatabase() {
    abstract fun stickerAppDao(): StickerAppDao

    companion object {
        @Volatile
        private var INSTANCE: StickerAppDatabase? = null

        fun getInstance(context: Context): StickerAppDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    StickerAppDatabase::class.java,
                    "sticker-app-db"
                ).build().also { INSTANCE = it }
            }
        }
    }
}