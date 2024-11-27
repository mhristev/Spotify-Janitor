package org.internship.kmp.martin.album.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
@Entity
data class AlbumEntity(
    @PrimaryKey(autoGenerate = false) val id: String,
    val name: String,
    val imageUrl: String
)
