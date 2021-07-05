package ru.jasfex.stikeros.create_stickerpack

import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import ru.jasfex.stikeros.R
import ru.jasfex.stikeros.StickerApp
import ru.jasfex.stikeros.data.entity.Sticker
import java.io.FileInputStream

class CreateStickerPackActivity : AppCompatActivity() {

    private lateinit var etStickerpackName: EditText
    private lateinit var rvStickers: RecyclerView
    private lateinit var btnCreateStickerpack: Button

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val textUri: TextView = itemView.findViewById(R.id.tv_uri)
        val imageSticker: ImageView = itemView.findViewById(R.id.iv_sticker)

        fun bind(
            sticker: StickerItem,
            onItemSelected: (item: Sticker) -> Unit,
            onItemUnselected: (item: Sticker) -> Unit
        ) {
            itemView.setOnClickListener {
                if (sticker.selected) {
                    onItemUnselected(sticker.sticker)
                } else {
                    onItemSelected(sticker.sticker)
                }
            }

            if (sticker.selected) {
                textUri.setTextColor(Color.GREEN)
            } else {
                textUri.setTextColor(Color.BLACK)
            }

            textUri.text = "${sticker.sticker.emoji} URI: ${sticker.sticker.uri}"

            val resolver = imageSticker.context.applicationContext.contentResolver
            val readOnlyMode = "r"
            resolver.openFileDescriptor(Uri.parse(sticker.sticker.uri), readOnlyMode).use { pfd ->
                val fd = pfd!!.fileDescriptor
                val fin = FileInputStream(fd)
                val bytes = fin.readBytes()
                val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                fin.close()
                imageSticker.setImageBitmap(bitmap)
            }
        }
    }

    class Adapter(
        private val onItemSelected: (item: Sticker) -> Unit,
        private val onItemUnselected: (item: Sticker) -> Unit
    ) : RecyclerView.Adapter<ViewHolder>() {

        private var data: List<StickerItem> = emptyList()

        fun setData(stickers: List<StickerItem>) {
            data = stickers
            notifyDataSetChanged()
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val itemView = inflater.inflate(R.layout.item_sticker, parent, false)
            return ViewHolder(itemView)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val item = data[position]
            holder.bind(
                sticker = item,
                onItemSelected = onItemSelected,
                onItemUnselected = onItemUnselected
            )
        }

        override fun getItemCount(): Int {
            return data.size
        }
    }

    private val uiScope = CoroutineScope(Dispatchers.Main)
    private lateinit var adapter: Adapter

    private fun generateStickerPackName(): String {
        return "StickerPack_${System.currentTimeMillis()}"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_stickerpack)

        val viewModel: CreateStickerPackViewModel by viewModels {
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                    return CreateStickerPackViewModel((application as StickerApp).stickerDao) as T
                }
            }
        }

        adapter = Adapter(viewModel::onSelectSticker, viewModel::onUnselectSticker)

        etStickerpackName = findViewById(R.id.et_stickerpack_name)
        rvStickers = findViewById(R.id.rv_stickers)
        btnCreateStickerpack = findViewById(R.id.btn_create_sticker_pack)

        etStickerpackName.setText(generateStickerPackName())

        uiScope.launch {
            viewModel.stickers.collect { stickers ->
                adapter.setData(stickers)
            }
        }

        rvStickers.layoutManager = LinearLayoutManager(this)
        rvStickers.adapter = adapter

        btnCreateStickerpack.setOnClickListener {
            val stickerPackName = etStickerpackName.text.toString().trim()
            viewModel.onCreateStickerPack(
                stickerPackName = stickerPackName
            ) {
                runOnUiThread {
                    Toast.makeText(this, "Sticker pack created!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}