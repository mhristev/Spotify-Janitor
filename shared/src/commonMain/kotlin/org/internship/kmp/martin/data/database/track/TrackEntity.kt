package org.internship.kmp.martin.data.database.track

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class TrackEntity (
    @PrimaryKey(autoGenerate = false) val id: String,
)