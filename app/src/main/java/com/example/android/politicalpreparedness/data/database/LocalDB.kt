package com.example.android.politicalpreparedness.data.database

import android.content.Context
import androidx.room.Room


/**
 * Created By Varsha Kulkarni on 02/02/21
 */
object LocalDB {

    fun createElectionsDao(context: Context): ElectionDao {

        return Room.databaseBuilder(
                context.applicationContext,
                ElectionDatabase::class.java,
                "election_database"
        ).build().electionDao

    }

}