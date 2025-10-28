package com.korniykom.chat_backend.user.domain.exception

class RateLimitException(
    resetsInSeconds: Long
) : RuntimeException(
    "Rate limit exceeded. Please try again in $resetsInSeconds seconds"
)