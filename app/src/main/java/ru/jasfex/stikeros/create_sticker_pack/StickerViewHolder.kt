package ru.jasfex.stikeros.create_sticker_pack

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.jasfex.stikeros.R
import ru.jasfex.stikeros.data.StickerEntity
import java.io.FileInputStream

class StickerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val tvSticker = itemView.findViewById<TextView>(R.id.tv_sticker)
    private val ivSticker = itemView.findViewById<ImageView>(R.id.iv_sticker)

    fun bind(
        item: StickerItem,
        onSelect: (sticker: StickerEntity) -> Unit,
        onUnselect: (sticker: StickerEntity) -> Unit
    ) {
        var bitmap: Bitmap? = null
        try {
            val uri = Uri.parse(item.sticker.uri)
            val resolver = ivSticker.context.applicationContext.contentResolver
            resolver.openFileDescriptor(uri, "r").use { pfd ->
                val fin = FileInputStream(pfd!!.fileDescriptor)
                val bytes = fin.readBytes()
                bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                fin.close()
            }
        } catch (th: Throwable) {
        }

        if (bitmap == null) {
            ivSticker.setBackgroundColor(Color.LTGRAY)
            ivSticker.setImageBitmap(null)
            itemView.setBackgroundColor(Color.WHITE)
            itemView.setOnClickListener(null)
            tvSticker.text = "Failed to load sticker from storage"
        }

        bitmap?.let { stickerBitmap ->
            ivSticker.setBackgroundColor(Color.TRANSPARENT)
            ivSticker.setImageBitmap(stickerBitmap)

            if (item.selected) {
                itemView.setBackgroundColor(Color.argb(255, 255, 219, 88))
                itemView.setOnClickListener { onUnselect(item.sticker) }
            } else {
                itemView.setBackgroundColor(Color.WHITE)
                itemView.setOnClickListener { onSelect(item.sticker) }
            }

            val stickerText = "Uri: ${item.sticker.uri}; Emoji = ${item.sticker.emoji}"
            tvSticker.text = stickerText
        }
    }
}