package com.korniykom.core.domain.logging

interface ChatLogger {
    fun info(message: String)
    fun debug(message: String)
    fun warning(message: String)
    fun error(message: String, throwable: Throwable? = null)
}