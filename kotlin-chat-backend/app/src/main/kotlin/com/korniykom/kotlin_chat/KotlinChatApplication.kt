package com.korniykom.kotlin_chat

import org.hibernate.dialect.Database
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class KotlinChatApplication

fun main(args: Array<String>) {
    runApplication<KotlinChatApplication>(*args)
}