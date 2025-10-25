package com.korniykom.chat_backend.user.domain.exception

class SamePasswordException: RuntimeException(
    "The new password can't be the same as your old one"
) {
}