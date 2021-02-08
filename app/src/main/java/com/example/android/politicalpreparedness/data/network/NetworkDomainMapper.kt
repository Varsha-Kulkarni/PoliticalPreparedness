package com.example.android.politicalpreparedness.data.network

import com.example.android.politicalpreparedness.data.network.models.ElectionFromNetwork
import com.example.android.politicalpreparedness.domain.models.Election


/**
 * Created By Varsha Kulkarni on 02/02/21
 */
fun List<ElectionFromNetwork>.toDomainModel(): List<Election>{
    return map{
        Election(
                id = it.id,
                name = it.name,
                electionDay = it.electionDay,
                division = it.division,
                isFollowed = false
        )
    }
}