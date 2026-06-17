package com.medical.buganddrug

import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts

object AndroidImagePicker {

    var launcher: ActivityResultLauncher<PickVisualMediaRequest>? = null

    var callback: ((List<ByteArray>) -> Unit)? = null

    fun register(launcher: ActivityResultLauncher<PickVisualMediaRequest>) {
        this.launcher = launcher
    }

    fun launch(callback: (List<ByteArray>) -> Unit) {
        this.callback = callback
        launcher?.launch(
            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
        )
    }
}