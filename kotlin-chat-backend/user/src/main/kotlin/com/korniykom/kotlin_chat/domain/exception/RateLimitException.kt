package com.korniykom.kotlin_chat.domain.exception

class RateLimitException(
    val resetsInSeconds: Long
): RuntimeException(
    "Rate limit exceeded. Please try in $resetsInSeconds seconds."
)