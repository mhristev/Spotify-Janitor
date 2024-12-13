package org.internship.kmp.martin.core.domain

sealed interface DataError: Error {
    enum class Remote: DataError {
        REQUEST_TIMEOUT,
        TOO_MANY_REQUESTS,
        NO_INTERNET,
        SERVER,
        SERIALIZATION,
        UNKNOWN,
        LIMIT_EXCEEDED,
    }

    enum class Local: DataError {
        UNKNOWN,
        NO_ACCESS_TOKEN,
        NO_DATA,
        ACCESS_TOKEN_EXPIRED
    }
}
