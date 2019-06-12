package ru.mbannikov.newrest

import org.springframework.core.ParameterizedTypeReference
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono
import reactor.util.function.component1
import reactor.util.function.component2
import ru.mbannikov.newrest.common.*
import java.lang.RuntimeException


class Client {
    private val webClient = WebClient.builder()
        .baseUrl("http://localhost:8080")
        .build()

    private val userResultType = object : ParameterizedTypeReference<ResultResponse<User>>() {}

    fun getUser(): Mono<DefaultResult<User>> {
        return webClient.get()
            .uri("/user")
            .retrieve()
            .bodyToMono(userResultType)
            .map(::mapResponseToResult)
    }

    fun getError(): Mono<DefaultResult<User>> {
        return webClient.get()
            .uri("/user?error")
            .retrieve()
            .bodyToMono(userResultType)
            .map(::mapResponseToResult)
    }

    private fun mapResponseToResult(result: ResultResponse<User>): DefaultResult<User> = when {
        result.data != null -> SuccessResult(result.data)
        result.error != null -> ErrorResult(result.error)
        else -> throw RuntimeException("Response has to contain \"data\" or \"error\" key")
    }

    private data class ResultResponse<T : Any>(
        val error: Error?,
        val data: T?
    )
}

fun printResult(result: DefaultResult<User>) = when (result) {
    is ErrorResult -> println("Error: ${result.error}")
    is SuccessResult<User> -> println("User: ${result.data}")
}

fun main(args: Array<String>) {
    val client = Client()

    Mono.zip(client.getError(), client.getUser())
        .map {
            val (error, user) = it

            println("-------------------------------------------")
            printResult(error)
            printResult(user)
            println("-------------------------------------------")
        }
        .block()
}