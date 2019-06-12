package ru.mbannikov.newrest.common


sealed class Result<out T : Any, out R : Any> {
    data class SuccessResult<out T : Any>(val data: T) : Result<T, Nothing>()
    data class ErrorResult<out R : Any>(val error: R) : Result<Nothing, R>()
}

sealed class DefaultResult<out T : Any> : Result<Error, T>()
data class SuccessResult<out T : Any>(val data: T) : DefaultResult<T>()
data class ErrorResult(val error: Error) : DefaultResult<Nothing>()