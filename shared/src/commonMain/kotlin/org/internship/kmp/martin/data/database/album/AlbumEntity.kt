package org.internship.kmp.martin.data.database.album

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
