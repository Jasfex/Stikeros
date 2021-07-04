package ru.jasfex.stikeros.create_sticker

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.jasfex.stikeros.Emojis
import ru.jasfex.stikeros.data.StickerosDao
import ru.jasfex.stikeros.data.entity.Sticker

class CreateStickerViewModel : ViewModel() {

    fun onSaveSticker(dao: StickerosDao, uri: Uri) {
        viewModelScope.launch(Dispatchers.IO) {
            val stickerEntity = Sticker(uri = uri.toString(), emoji = Emojis[0])
            dao.saveSticker(stickerEntity)
        }
    }

}