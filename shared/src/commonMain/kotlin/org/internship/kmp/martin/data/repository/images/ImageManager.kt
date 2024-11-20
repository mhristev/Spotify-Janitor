package org.internship.kmp.martin.data.repository.images

interface ImageManager<T> {
    fun save(entity: T): String
}