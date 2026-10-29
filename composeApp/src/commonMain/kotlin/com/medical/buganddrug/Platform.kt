package com.medical.buganddrug

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform
expect fun getAppVersion(): String
expect fun pickImages(
    onImagesPicked: (List<ByteArray>) -> Unit
)