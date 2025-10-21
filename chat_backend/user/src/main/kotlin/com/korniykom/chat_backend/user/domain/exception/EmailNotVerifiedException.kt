package com.korniykom.chat_backend.user.domain.exception

class EmailNotVerifiedException: RuntimeException(
    "Email is not verified"
) {
}