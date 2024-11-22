package org.internship.kmp.martin.data.database

import androidx.room.RoomDatabaseConstructor

@Suppress("NO_ACTUAL_FOR_EXPECT")
expect object AppDatabaseConstructor : RoomDatabaseConstructor<RoomAppDatabase> {
    override fun initialize(): RoomAppDatabase
}