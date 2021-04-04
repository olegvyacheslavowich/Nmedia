package ru.netology.nmedia.errors

import java.lang.RuntimeException

sealed class AppError(val code: String): RuntimeException()
class ApiError(val status: Int, code: String) : AppError(code)
class NetworkError : AppError("error_network")
class UnknownError : AppError("error_unknown")
