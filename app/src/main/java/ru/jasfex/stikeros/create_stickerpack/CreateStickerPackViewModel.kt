package ru.jasfex.stikeros.create_stickerpack

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import ru.jasfex.stikeros.data.StickerosDao
import ru.jasfex.stikeros.data.entity.Sticker
import ru.jasfex.stikeros.data.entity.Stickerpack
import ru.jasfex.stikeros.data.entity.StickerpackStickerCrossRef

class CreateStickerPackViewModel(
    private val dao: StickerosDao
) : ViewModel() {

    private val allStickers = dao.getStickersFlow()
    private val selectedStickers = MutableStateFlow(emptyList<Sticker>())

    val stickers: Flow<List<StickerItem>> = allStickers.combine(selectedStickers) { all, selected ->
        all.map { StickerItem(it, selected.contains(it)) }
    }

    fun onSelectSticker(sticker: Sticker) {
        selectedStickers.value = selectedStickers.value.toMutableList().apply { add(sticker) }
    }

    fun onUnselectSticker(sticker: Sticker) {
        selectedStickers.value =
            selectedStickers.value.toMutableList().apply { removeAll { it == sticker } }
    }

    init {
        viewModelScope.launch(Dispatchers.Default) {
            stickers.collect {
                println("SALAM $it")
            }
        }
    }

    fun onCreateStickerPack(
        stickerPackName: String,
        onSaved: () -> Unit
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            val stickerPack = Stickerpack(name = stickerPackName)
            val stickerPackUid = dao.saveStickerpack(stickerPack)
            selectedStickers.value.forEach { sticker ->
                val crossRef = StickerpackStickerCrossRef(
                    stickerpackUid = stickerPackUid,
                    stickerUid = sticker.stickerUid
                )
                dao.saveCrossRefs(crossRef)
            }
            onSaved()
        }
    }

}