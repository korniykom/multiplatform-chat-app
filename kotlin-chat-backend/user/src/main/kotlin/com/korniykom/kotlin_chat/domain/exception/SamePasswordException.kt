package com.korniykom.kotlin_chat.domain.exception

class SamePasswordException: RuntimeException(
    "The new password cannot be the same as the old one"
)