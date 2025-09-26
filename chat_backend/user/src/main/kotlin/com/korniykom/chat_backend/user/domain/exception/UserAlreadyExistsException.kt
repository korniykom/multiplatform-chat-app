package com.korniykom.chat_backend.user.domain.exception

class UserAlreadyExistsException: RuntimeException(
    "A user with this username or email already exists."
) {
}