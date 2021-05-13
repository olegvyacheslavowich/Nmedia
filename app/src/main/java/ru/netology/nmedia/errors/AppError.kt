package ru.netology.nmedia.errors

import java.sql.SQLException

sealed class AppError(val code: String) : RuntimeException() {
    companion object {
        fun from(e: Throwable) = when (e) {
            is AppError -> e
            is DBError -> DBError
            is SQLException -> NetworkError
            else -> UnknownError
        }
    }
}

class ApiError(val status: Int, code: String) : AppError(code)
object DBError : AppError("error_database")
object NetworkError : AppError("error_network")
object UnknownError : AppError("error_unknown")
