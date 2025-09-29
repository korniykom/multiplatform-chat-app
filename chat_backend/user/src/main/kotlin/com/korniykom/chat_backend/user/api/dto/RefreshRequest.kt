package com.korniykom.chat_backend.user.api.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class RefreshRequest(
    @JsonProperty("refreshToken")
    val refreshToken: String
)
