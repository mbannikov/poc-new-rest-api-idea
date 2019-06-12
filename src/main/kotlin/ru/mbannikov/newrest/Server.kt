package ru.mbannikov.newrest

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.router
import reactor.core.publisher.Mono
import ru.mbannikov.newrest.common.*


@Configuration
class Router {
    @Bean
    fun route() = router {
        GET("/user") { request ->
            val returnError = request.queryParam("error").isPresent

            if (returnError) {
                Error(100, "Some error description").toResponse()
            } else {
                User(1, "admin").toResponse()
            }
        }
    }
}

fun Error.toResponse(): Mono<ServerResponse> =
    ServerResponse.ok().syncBody(
        ErrorResult(this)
    )

fun Any.toResponse(): Mono<ServerResponse> =
    ServerResponse.ok().syncBody(
        SuccessResult(this)
    )


@SpringBootApplication
class Server

fun main(args: Array<String>) {
    runApplication<Server>(*args)
}