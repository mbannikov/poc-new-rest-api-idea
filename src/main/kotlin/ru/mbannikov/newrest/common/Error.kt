package ru.mbannikov.newrest.common

data class Error(
    val code: Int,
    val message: String
)