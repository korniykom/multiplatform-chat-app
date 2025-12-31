package com.korniykom.kotlin_chat.domain.exception

class UnauthorizedException : RuntimeException(
    "Missing auth details."
) {
}