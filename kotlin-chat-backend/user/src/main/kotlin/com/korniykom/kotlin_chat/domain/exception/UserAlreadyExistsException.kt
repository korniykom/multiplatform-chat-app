package com.korniykom.kotlin_chat.domain.exception

class UserAlreadyExistsException: RuntimeException(
    "A user already exists"
)