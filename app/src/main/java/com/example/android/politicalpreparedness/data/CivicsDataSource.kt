package com.example.android.politicalpreparedness.data

import androidx.lifecycle.LiveData
import com.example.android.politicalpreparedness.data.database.entities.ElectionEntity
import com.example.android.politicalpreparedness.domain.models.Election


/**
 * Created By Varsha Kulkarni on 02/02/21
 */
interface CivicsDataSource {
    val electionsFollowed: LiveData<List<Election>>
    val electionsUpcoming: LiveData<List<Election>>
    suspend fun refreshElectionsData()
    suspend fun saveElection(election: ElectionEntity)
    fun getSavedElections(): LiveData<List<ElectionEntity>>
    fun getElectionById(id: Int) : LiveData<ElectionEntity>
    suspend fun deleteElection(election: ElectionEntity)
    suspend fun clear()
}