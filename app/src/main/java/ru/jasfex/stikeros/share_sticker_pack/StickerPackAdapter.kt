package ru.jasfex.stikeros.share_sticker_pack

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.jasfex.stikeros.R
import ru.jasfex.stikeros.data.StickerPackWithStickers

class StickerPackAdapter : RecyclerView.Adapter<StickerPackViewHolder>() {

    private var items = listOf<StickerPackWithStickers>()

    private var onClick: (stickerUriList: ArrayList<String>, stickerEmojiList: ArrayList<String>) -> Unit =
        { a, b -> }

    fun setItems(items: List<StickerPackWithStickers>) {
        this.items = items
        notifyDataSetChanged()
    }

    fun setOnClick(func: (stickerUriList: ArrayList<String>, stickerEmojiList: ArrayList<String>) -> Unit) {
        onClick = func
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StickerPackViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemView = inflater.inflate(R.layout.item_sticker_pack, parent, false)
        return StickerPackViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: StickerPackViewHolder, position: Int) {
        holder.bind(items[position], onClick)
    }

    override fun getItemCount(): Int {
        return items.size
    }
}