package com.korniykom.chat_backend.user.domain.exception

class InvalidCredentialsException: RuntimeException(
    "The entered credentials aren't valid"
) {
}