package com.korniykom.kotlin_chat.domain.exception

class UserAlreadyExists: RuntimeException(
    "A user already exists"
)