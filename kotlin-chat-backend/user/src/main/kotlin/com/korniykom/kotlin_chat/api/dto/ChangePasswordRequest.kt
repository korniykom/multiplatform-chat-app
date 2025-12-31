package com.korniykom.kotlin_chat.api.dto

import com.korniykom.kotlin_chat.api.util.Password

class ChangePasswordRequest(
    @field:Password
    val newPassword: String,
    @field:Password
    val oldPassword: String
) {
}