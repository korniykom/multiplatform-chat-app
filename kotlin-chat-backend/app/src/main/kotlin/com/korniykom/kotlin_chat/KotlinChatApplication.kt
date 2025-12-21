package com.korniykom.kotlin_chat

import com.korniykom.kotlin_chat.infra.database.entities.UserEntity
import com.korniykom.kotlin_chat.infra.database.repositories.UserRepository
import jakarta.annotation.PostConstruct
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.stereotype.Component

@SpringBootApplication
class KotlinChatApplication

fun main(args: Array<String>) {
    runApplication<KotlinChatApplication>(*args)
}

@Component
class Demo(
    private val repository: UserRepository
) {
    @PostConstruct
    fun init() {
        repository.save(
            UserEntity(
                email = "something@test.com",
                username = "name",
                hashedPassword = "123"
            )
        )
    }
}
