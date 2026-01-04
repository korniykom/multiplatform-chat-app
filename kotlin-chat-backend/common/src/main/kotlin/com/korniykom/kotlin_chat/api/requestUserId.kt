package com.korniykom.kotlin_chat.api

import com.korniykom.kotlin_chat.domain.exception.UnauthorizedException
import com.korniykom.kotlin_chat.type.UserId
import org.springframework.security.core.context.SecurityContextHolder

val requestUserId: UserId
    get() = SecurityContextHolder.getContext().authentication?.principal as? UserId
        ?: throw UnauthorizedException()