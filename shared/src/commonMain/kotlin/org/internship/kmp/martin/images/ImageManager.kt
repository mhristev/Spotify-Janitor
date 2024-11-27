package org.internship.kmp.martin.images

interface ImageManager<T> {
    fun save(entity: T): String
}