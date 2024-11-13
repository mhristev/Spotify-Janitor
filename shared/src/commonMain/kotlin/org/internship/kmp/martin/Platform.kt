package org.internship.kmp.martin

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform