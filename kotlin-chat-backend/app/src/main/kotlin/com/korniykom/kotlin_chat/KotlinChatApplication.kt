package com.korniykom.kotlin_chat

import org.hibernate.dialect.Database
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@EnableScheduling
class KotlinChatApplication

fun main(args: Array<String>) {
    runApplication<KotlinChatApplication>(*args)
}