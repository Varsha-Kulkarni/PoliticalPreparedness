package com.example.android.politicalpreparedness.data.database.entities

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.android.politicalpreparedness.data.network.models.Division
import kotlinx.android.parcel.Parcelize
import java.util.*


/**
 * Created By Varsha Kulkarni on 02/02/21
 */
@Entity(tableName = "election_table")
@Parcelize
data class ElectionEntity(
        @PrimaryKey val id: Int,
        @ColumnInfo(name = "name")val name: String,
        @ColumnInfo(name = "electionDay")val electionDay: Date,
        @Embedded(prefix = "division_") val division: Division,
        @ColumnInfo(name = "isFollowed")val isFollowed: Boolean = false
): Parcelable