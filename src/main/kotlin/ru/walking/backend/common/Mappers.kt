package ru.walking.backend.common

import org.springframework.http.ResponseEntity

fun <T> T.ok() = ResponseEntity.ok(this)
