package ru.jasfex.stikeros.share_stickerpack

import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import ru.jasfex.stikeros.R
import ru.jasfex.stikeros.StickerApp
import ru.jasfex.stikeros.data.entity.relation.StickerpackWithStickers
import ru.jasfex.stikeros.domain.shareStickers
import java.io.FileInputStream

class ShareStickerpackActivity : AppCompatActivity() {

    private class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val name: TextView = itemView.findViewById(R.id.tv_stickerpack_name)
        private val stickers: LinearLayoutCompat = itemView.findViewById(R.id.stickers)

        fun bind(
            stickerPack: StickerpackWithStickers,
            onItemSelected: (pack: StickerpackWithStickers) -> Unit
        ) {
            itemView.setOnClickListener { onItemSelected(stickerPack) }

            name.text = stickerPack.stickerpack.name
            stickers.removeAllViews()
            stickerPack.stickers.forEach { sticker ->
                val imageView = ImageView(itemView.context)
                imageView.layoutParams = ViewGroup.LayoutParams(128, 128)
                val resolver = itemView.context.applicationContext.contentResolver
                val readOnlyMode = "r"
                resolver.openFileDescriptor(Uri.parse(sticker.uri), readOnlyMode).use { pfd ->
                    val fd = pfd!!.fileDescriptor
                    val fin = FileInputStream(fd)
                    val bytes = fin.readBytes()
                    val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                    fin.close()
                    imageView.setImageBitmap(bitmap)
                }
                stickers.addView(imageView)
            }
        }
    }

    private class Adapter(
        private val onItemSelected: (pack: StickerpackWithStickers) -> Unit
    ) : RecyclerView.Adapter<ViewHolder>() {

        private var stickerPacks: List<StickerpackWithStickers> = emptyList()

        fun setStickerPacks(packs: List<StickerpackWithStickers>) {
            stickerPacks = packs
            notifyDataSetChanged()
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val itemView = inflater.inflate(R.layout.item_stickerpack, parent, false)
            return ViewHolder(itemView)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val stickerPack = stickerPacks[position]
            holder.bind(stickerPack, onItemSelected)
        }

        override fun getItemCount(): Int {
            return stickerPacks.size
        }
    }

    private lateinit var rvStickerPacks: RecyclerView
    private lateinit var adapter: Adapter
    private lateinit var viewModel: ShareStickerpackViewModel

    private val uiScope = CoroutineScope(Dispatchers.Main)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_share_stickerpack)

        viewModel = ShareStickerpackViewModel((application as StickerApp).stickerDao)
        adapter = Adapter {
            val stickers = ArrayList<Uri>()
            val emojis = ArrayList<String>()

            stickers.addAll(it.stickers.map { Uri.parse(it.uri) })
            emojis.addAll(it.stickers.map { it.emoji })

            shareStickers(stickers, emojis)
        }

        uiScope.launch {
            viewModel.stickerPacks.collect {
                adapter.setStickerPacks(it)
            }
        }

        rvStickerPacks = findViewById(R.id.rv_stickerpacks)
        rvStickerPacks.layoutManager = LinearLayoutManager(this)
        rvStickerPacks.adapter = adapter
    }
}