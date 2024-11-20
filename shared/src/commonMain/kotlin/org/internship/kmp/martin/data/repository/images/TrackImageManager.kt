package org.internship.kmp.martin.data.repository.images

import org.internship.kmp.martin.data.domain.Track

class TrackImageManager : ImageManager<Track> {
    override fun save(entity: Track): String {
        return "Track image saved for"
    }
}