package ru.jasfex.stikeros.create_sticker_pack

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.jasfex.stikeros.R
import ru.jasfex.stikeros.data.StickerEntity

class StickerAdapter : RecyclerView.Adapter<StickerViewHolder>() {

    private var items = emptyList<StickerItem>()

    private var onSelect: (sticker: StickerEntity) -> Unit = {}
    private var onUnselect: (sticker: StickerEntity) -> Unit = {}

    fun setOnSelect(func: (sticker: StickerEntity) -> Unit) {
        onSelect = func
    }

    fun setOnUnselect(func: (sticker: StickerEntity) -> Unit) {
        onUnselect = func
    }

    fun setItems(items: List<StickerItem>) {
        this.items = items
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StickerViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_sticker, parent, false)
        return StickerViewHolder(view)
    }

    override fun onBindViewHolder(holder: StickerViewHolder, position: Int) {
        holder.bind(items[position], onSelect, onUnselect)
    }

    override fun getItemCount(): Int {
        return items.size
    }
}