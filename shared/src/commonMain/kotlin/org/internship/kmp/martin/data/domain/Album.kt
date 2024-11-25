package org.internship.kmp.martin.data.domain

import kotlinx.serialization.Serializable
import org.internship.kmp.martin.data.dto.ImageDto
@Serializable
data class Album(
    val id: String,
    val name: String,
    val imageUrl: String
)
