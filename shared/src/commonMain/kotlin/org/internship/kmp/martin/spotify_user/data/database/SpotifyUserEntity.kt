package org.internship.kmp.martin.spotify_user.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
@Entity
data class SpotifyUserEntity (
    @PrimaryKey(autoGenerate = false) val id: String,
    val country: String,
    val displayName: String,
    val email: String,
    val product: String,
    val imageUrl: String,
    val followers: Int
)
