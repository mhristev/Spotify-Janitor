package org.internship.kmp.martin.core.domain

//sealed interface DataError: Error {
//    enum class Remote: DataError {
//        REQUEST_TIMEOUT,
//        TOO_MANY_REQUESTS,
//        NO_INTERNET,
//        SERVER,
//        SERIALIZATION,
//        UNKNOWN
//    }
//
//    enum class Local: DataError {
//        DISK_FULL,
//        UNKNOWN
//    }
//}

sealed class DataError: Error, Throwable() {
    sealed class Remote: DataError() {
        object REQUEST_TIMEOUT: Remote()
        object TOO_MANY_REQUESTS: Remote()
        object NO_INTERNET: Remote()
        object SERVER: Remote()
        object SERIALIZATION: Remote()
        object UNKNOWN: Remote()
    }

    sealed class Local: DataError() {
        object DISK_FULL: Local()
        object UNKNOWN: Local()
    }
}