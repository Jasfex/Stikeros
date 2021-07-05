package ru.jasfex.stikeros

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import ru.jasfex.stikeros.create_sticker.CreateStickerActivity
import ru.jasfex.stikeros.create_stickerpack.CreateStickerPackActivity
import ru.jasfex.stikeros.share_stickerpack.ShareStickerpackActivity


class MainActivity : AppCompatActivity() {

    sealed class State {
        object PermissionDenied : State()
        object PermissionGranted : State()
    }

    private fun getState(): State {
        val permissionGranted = ContextCompat
            .checkSelfPermission(
                this, Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        return if (permissionGranted) {
            State.PermissionGranted
        } else {
            State.PermissionDenied
        }
    }

    private val state: MutableStateFlow<State> = MutableStateFlow(State.PermissionDenied)
    private val uiScope = CoroutineScope(Dispatchers.Main)

    private lateinit var buttonsContainer: LinearLayoutCompat
    private lateinit var btnCreateSticker: Button
    private lateinit var btnCreateStickerPack: Button
    private lateinit var btnShareStickerPack: Button

    private lateinit var permissionContainer: LinearLayoutCompat
    private lateinit var tvPermissionRationale: TextView
    private lateinit var btnProvidePermission: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        buttonsContainer = findViewById(R.id.buttons_container)!!
        btnCreateSticker = findViewById(R.id.btn_create_sticker)!!
        btnCreateStickerPack = findViewById(R.id.btn_create_sticker_pack)!!
        btnShareStickerPack = findViewById(R.id.btn_share_sticker_pack)!!
        bindDestination(btnCreateSticker, CreateStickerActivity::class.java)
        bindDestination(btnCreateStickerPack, CreateStickerPackActivity::class.java)
        bindDestination(btnShareStickerPack, ShareStickerpackActivity::class.java)

        permissionContainer = findViewById(R.id.permission_container)!!
        tvPermissionRationale = findViewById(R.id.tv_permission_rationale)!!
        btnProvidePermission = findViewById(R.id.btn_provide_permission)!!
        btnProvidePermission.setOnClickListener {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                REQUEST_PERMISSION_CODE
            )
        }

        state.value = getState()
    }

    override fun onStart() {
        super.onStart()
        uiScope.launch {
            state.collect {
                when (it) {
                    State.PermissionDenied -> {
                        showPermissionsBlock(true)
                        showContentBlock(false)
                    }
                    State.PermissionGranted -> {
                        showPermissionsBlock(false)
                        showContentBlock(true)
                    }
                }
            }
        }
    }

    override fun onStop() {
        super.onStop()
        uiScope.cancel()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == REQUEST_PERMISSION_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                state.value = State.PermissionGranted
            } else {
                state.value = State.PermissionDenied
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    private fun showPermissionsBlock(show: Boolean) {
        val visibility = if (show) View.VISIBLE else View.GONE
        permissionContainer.visibility = visibility
        tvPermissionRationale.visibility = visibility
        btnProvidePermission.visibility = visibility
    }

    private fun showContentBlock(show: Boolean) {
        val visibility = if (show) View.VISIBLE else View.GONE
        buttonsContainer.visibility = visibility
        btnCreateSticker.visibility = visibility
        btnCreateStickerPack.visibility = visibility
        btnShareStickerPack.visibility = visibility
    }

    private fun <T> bindDestination(button: Button, clazz: Class<T>) {
        button.setOnClickListener {
            startActivity(Intent(button.context, clazz))
        }
    }

    companion object {
        const val REQUEST_PERMISSION_CODE = 777
    }

}