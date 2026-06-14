package com.medical.buganddrug

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform