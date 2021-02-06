package com.example.android.politicalpreparedness.domain.models

import android.os.Parcelable
import com.example.android.politicalpreparedness.data.database.entities.ElectionEntity
import com.example.android.politicalpreparedness.data.network.models.Division
import kotlinx.android.parcel.Parcelize
import java.util.*


/**
 * Created By Varsha Kulkarni on 02/02/21
 */
@Parcelize
data class Election(
        val id: Int,
        val name: String,
        val electionDay: Date,
        val division: Division,
        var isFollowed: Boolean = false): Parcelable

    fun Election.toDatabaseEntity(): ElectionEntity{
        return ElectionEntity(
                id = this.id,
                name = this.name,
                division = this.division,
                electionDay = this.electionDay,
                isFollowed = this.isFollowed
        )
    }
