package org.internship.kmp.martin.artist.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
@Entity
data class ArtistEntity(
    @PrimaryKey(autoGenerate = false) val id: String,
    val name: String,
)
