package ru.jasfex.stikeros.create_sticker_pack

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import ru.jasfex.stikeros.R
import ru.jasfex.stikeros.StickerApp
import ru.jasfex.stikeros.data.StickerEntity
import ru.jasfex.stikeros.data.StickerPackEntity
import ru.jasfex.stikeros.data.StickersCrossRef

class CreateStickerPackActivity : AppCompatActivity() {

    private val app: StickerApp get() = application as StickerApp

    private val adapter = StickerAdapter()

    private var ioScope: CoroutineScope? = null

    private val selectedStickers = mutableListOf<StickerEntity>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_sticker_pack)

        ioScope = CoroutineScope(Dispatchers.IO)

        val etStickerPackName = findViewById<TextInputEditText>(R.id.et_sticker_pack_name)!!
        val rvStickers = findViewById<RecyclerView>(R.id.rv_stickers)!!
        val btnSaveStickerPack = findViewById<Button>(R.id.btn_save_sticker_pack)!!

        setupStickerPackName(etStickerPackName)
        setupStickers(rvStickers)
        setupSaveStickerPack(btnSaveStickerPack, etStickerPackName)

        ioScope?.launch {
            app.stickerAppDao.getStickersFlow().collect { stickers ->
                val items = stickers.map { StickerItem(it, false) }
                adapter.setItems(items)
                adapter.setOnSelect { sticker ->
                    selectedStickers.add(sticker)
                    adapter.setItems(items.map { it.copy(selected = selectedStickers.contains(it.sticker)) })
                }
                adapter.setOnUnselect { sticker ->
                    selectedStickers.removeAll { it == sticker }
                    adapter.setItems(items.map { it.copy(selected = selectedStickers.contains(it.sticker)) })
                }
            }
        }
    }

    override fun onDestroy() {
        ioScope?.cancel()
        super.onDestroy()
    }

    private fun setupSaveStickerPack(
        btnSaveStickerPack: Button,
        etStickerPackName: TextInputEditText
    ) {
        btnSaveStickerPack.setOnClickListener {
            val stickerPackName = etStickerPackName.text.toString()
            val stickerUidList = selectedStickers.map { it.stickerUid }

            if (stickerUidList.isEmpty()) {
                Toast.makeText(this, "Sticker pack is empty! Select stickers!", Toast.LENGTH_SHORT).show()
            } else {
                ioScope?.launch {
                    val stickerPack = StickerPackEntity(name = stickerPackName)
                    val stickerPackUid = app.stickerAppDao.saveStickerPack(stickerPack)
                    for (stickerUid in stickerUidList) {
                        val crossRef = StickersCrossRef(stickerPackUid, stickerUid)
                        app.stickerAppDao.saveStickersCrossRef(crossRef)
                    }
                    withContext(Dispatchers.Main) {
                        Toast.makeText(
                            this@CreateStickerPackActivity,
                            "Sticker pack \"$stickerPackName\" saved!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }

    private fun setupStickers(rvStickers: RecyclerView) {
        rvStickers.layoutManager = LinearLayoutManager(this)
        rvStickers.adapter = adapter
    }

    private fun setupStickerPackName(etStickerPackName: TextInputEditText) {
        val generatedName = "Sticker Pack ${System.currentTimeMillis()}"
        etStickerPackName.setText(generatedName)
    }
}
