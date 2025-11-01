package com.korniykom.chat_backend.user.api.util

import com.korniykom.chat_backend.user.domain.exception.UnauthorizedException
import com.korniykom.chat_backend.user.domain.model.UserId
import org.springframework.security.core.context.SecurityContextHolder

val requestUserId: UserId
    get() = SecurityContextHolder.getContext().authentication?.principal as? UserId
        ?: throw UnauthorizedException()