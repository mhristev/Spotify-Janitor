package org.internship.kmp.martin.artist.domain

import kotlinx.serialization.Serializable

@Serializable
class Artist (
    val id: String,
    val name: String,
)