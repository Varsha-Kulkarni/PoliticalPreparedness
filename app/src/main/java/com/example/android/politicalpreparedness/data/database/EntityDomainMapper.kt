package com.example.android.politicalpreparedness.data.database

import com.example.android.politicalpreparedness.data.database.entities.ElectionEntity
import com.example.android.politicalpreparedness.domain.models.Election


/**
 * Created By Varsha Kulkarni on 02/02/21
 */
fun List<Election>.toDatabaseEntity(): Array<ElectionEntity>{
    return map{
        ElectionEntity(
                id = it.id,
                name = it.name,
                division = it.division,
                electionDay = it.electionDay,
                isFollowed = it.isFollowed
        )
    }.toTypedArray()
}

fun List<ElectionEntity>.toDomainModel(): List<Election>{
    return map{
        Election(
                id = it.id,
                name = it.name,
                division = it.division,
                electionDay = it.electionDay,
                isFollowed = it.isFollowed
        )
    }
}