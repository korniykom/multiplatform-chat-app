package com.korniykom.chat_backend.user.domain.exception

class UnauthorizedException: RuntimeException("Missing auth details") {
}