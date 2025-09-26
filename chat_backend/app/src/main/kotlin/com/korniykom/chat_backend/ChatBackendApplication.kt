package com.korniykom.chat_backend

import com.korniykom.chat_backend.user.infra.database.entity.UserEntity
import com.korniykom.chat_backend.user.infra.database.repositories.UserRepository
import jakarta.annotation.PostConstruct
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.stereotype.Component

@SpringBootApplication
class ChatBackendApplication

fun main(args: Array<String>) {
	runApplication<ChatBackendApplication>(*args)
}
