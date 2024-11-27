package org.internship.kmp.martin.images

import org.internship.kmp.martin.track.domain.Track

class TrackImageManager : ImageManager<Track> {
    override fun save(entity: Track): String {
        return "Track image saved for"
    }
}