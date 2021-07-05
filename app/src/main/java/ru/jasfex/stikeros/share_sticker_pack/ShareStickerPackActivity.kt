package ru.jasfex.stikeros.share_sticker_pack

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import ru.jasfex.stikeros.R
import ru.jasfex.stikeros.StickerApp

class ShareStickerPackActivity : AppCompatActivity() {

    private val app get() = application as StickerApp

    private val adapter = StickerPackAdapter()

    private val ioScope = CoroutineScope(Dispatchers.Main)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_share_sticker_pack)

        val rvStickerPacks = findViewById<RecyclerView>(R.id.rv_sticker_packs)!!

        rvStickerPacks.layoutManager = LinearLayoutManager(this)
        rvStickerPacks.adapter = adapter

        adapter.setOnClick { stickerUriList, stickerEmojiList ->
            val stickers = ArrayList<Uri>()
            stickers.addAll(stickerUriList.map { Uri.parse(it) })
            shareStickers(stickers, stickerEmojiList)
        }

        ioScope.launch {
            app.stickerAppDao.getStickerPacksWithStickers().collect {
                adapter.setItems(it)
            }
        }
    }
}