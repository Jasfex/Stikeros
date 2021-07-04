package ru.jasfex.stikeros.share_stickerpack

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.jasfex.stikeros.data.StickerosDao
import ru.jasfex.stikeros.data.entity.relation.StickerpackWithStickers

class ShareStickerpackViewModel(
    private val dao: StickerosDao
) : ViewModel() {

    val stickerPacks: MutableStateFlow<List<StickerpackWithStickers>> = MutableStateFlow(emptyList())

    init {
        viewModelScope.launch(Dispatchers.Default) {
            stickerPacks.value = dao.getStickerpackWithStickers()
        }
    }

}