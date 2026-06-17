package com.medical.buganddrug

import android.os.Build

class AndroidPlatform : Platform {
    override val name: String = "Android ${Build.VERSION.SDK_INT}"
}

actual fun getPlatform(): Platform = AndroidPlatform()
actual fun pickImages(
    onImagesPicked: (List<ByteArray>) -> Unit
) {
    AndroidImagePicker.launch(onImagesPicked)
}