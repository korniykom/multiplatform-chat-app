package com.korniykom.chat

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform