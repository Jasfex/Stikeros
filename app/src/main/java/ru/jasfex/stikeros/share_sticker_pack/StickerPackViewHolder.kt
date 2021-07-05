package ru.jasfex.stikeros.share_sticker_pack

import android.graphics.BitmapFactory
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.recyclerview.widget.RecyclerView
import ru.jasfex.stikeros.R
import ru.jasfex.stikeros.data.StickerPackWithStickers
import java.io.FileInputStream

class StickerPackViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val container =
        itemView.findViewById<LinearLayoutCompat>(R.id.sticker_pack_container)
    private val tvStickerPackName = itemView.findViewById<TextView>(R.id.tv_sticker_pack_name)

    fun bind(
        item: StickerPackWithStickers,
        onClick: (stickerUriList: ArrayList<String>, stickerEmojiList: ArrayList<String>) -> Unit
    ) {
        val stickerUriList= ArrayList<String>().apply { addAll(item.stickers.map { it.uri }) }
        val stickerEmojiList= ArrayList<String>().apply { addAll(item.stickers.map { it.emoji }) }
        container.setOnClickListener {
            onClick(stickerUriList, stickerEmojiList)
        }
        tvStickerPackName.setOnClickListener {
            onClick(stickerUriList, stickerEmojiList)
        }

        tvStickerPackName.setText(item.stickerPack.name)
        container.removeAllViews()
        container.removeAllViewsInLayout()
        item.stickers.forEach { sticker ->
            try {
                val ivSticker = LayoutInflater.from(container.context)
                    .inflate(R.layout.item_sticker_preview, container, false) as ImageView
                val resolver = itemView.context.applicationContext.contentResolver
                val readOnlyMode = "r"
                resolver.openFileDescriptor(Uri.parse(sticker.uri), readOnlyMode).use { pfd ->
                    val fd = pfd!!.fileDescriptor
                    val fin = FileInputStream(fd)
                    val bytes = fin.readBytes()
                    val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                    fin.close()
                    ivSticker.setImageBitmap(bitmap)
                }
                if (ivSticker.parent != null) {
                    (ivSticker.parent as? ViewGroup)?.removeView(ivSticker)
                }
                container.addView(ivSticker)
            } catch (th: Throwable) {

            }
        }
    }
}