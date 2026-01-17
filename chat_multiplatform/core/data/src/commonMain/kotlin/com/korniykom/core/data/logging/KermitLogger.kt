package com.korniykom.core.data.logging

import co.touchlab.kermit.Logger
import com.korniykom.core.domain.logging.ChatLogger

object KermitLogger : ChatLogger {
    override fun info(message: String) {
        Logger.i(message)
    }

    override fun debug(message: String) {
        Logger.d(message)
    }

    override fun warning(message: String) {
        Logger.w(message)

    }

    override fun error(message: String, throwable: Throwable?) {
        Logger.e(message, throwable)
    }
}