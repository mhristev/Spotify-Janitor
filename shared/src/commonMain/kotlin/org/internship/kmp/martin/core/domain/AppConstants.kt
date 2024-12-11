package org.internship.kmp.martin.core.domain

object AppConstants {
    object Limits {
        const val MAX_TRACKS_CACHED = 1000
        const val MIN_TRACKS_TO_DISPLAY = 50
        const val TRACKS_PER_LOAD_MORE = 50
    }

    object Colors {
        const val PRIMARY_DARK_HEX ="#03001C"
        const val PRIMARY_PURPLE_HEX = "#301E67"
        const val SPOTIFY_GREEN_HEX = "#2DBC58"
        const val SECONDARY_TEXT_GREY_HEX = "#949494"
        const val PRIMARY_TEXT_WHiTE_HEX = "#FAF9F6"
    }

    object VaultKeys {
        const val AUTH_TOKEN = "auth_token"
        const val EXPIRE_TIME = "expire_time"
        const val USER_ID = "user_id"
    }
}