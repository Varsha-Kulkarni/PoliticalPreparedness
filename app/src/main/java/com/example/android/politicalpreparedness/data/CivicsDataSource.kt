package com.example.android.politicalpreparedness.data

import androidx.lifecycle.LiveData
import com.example.android.politicalpreparedness.data.database.entities.ElectionEntity
import com.example.android.politicalpreparedness.data.network.models.Address
import com.example.android.politicalpreparedness.data.network.models.VoterInfoResponse
import com.example.android.politicalpreparedness.domain.models.Election
import com.example.android.politicalpreparedness.domain.models.Result
import com.example.android.politicalpreparedness.presentation.representative.model.Representative


/**
 * Created By Varsha Kulkarni on 02/02/21
 */
interface CivicsDataSource {
    val electionsFollowed: LiveData<List<Election>>
    val electionsUpcoming: LiveData<List<Election>>
    suspend fun refreshElectionsData()
    suspend fun updateElection(election: ElectionEntity)
    fun getElectionById(id: Int) : LiveData<ElectionEntity>
    suspend fun deleteElection(election: ElectionEntity)
    suspend fun clear()
    suspend fun getRepresentatives(address: Address) : Result<List<Representative>>
    suspend fun getVoterInfo(election: Election): Result<VoterInfoResponse>
}