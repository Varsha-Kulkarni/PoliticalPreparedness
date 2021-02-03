package com.example.android.politicalpreparedness.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.example.android.politicalpreparedness.data.CivicsDataSource
import com.example.android.politicalpreparedness.data.database.ElectionDao
import com.example.android.politicalpreparedness.data.database.entities.ElectionEntity
import com.example.android.politicalpreparedness.data.database.toDatabaseEntity
import com.example.android.politicalpreparedness.data.database.toDomainModel
import com.example.android.politicalpreparedness.data.network.toDomainModel
import com.example.android.politicalpreparedness.data.network.CivicsApi
import com.example.android.politicalpreparedness.domain.models.Election
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber


/**
 * Created By Varsha Kulkarni on 02/02/21
 */
class CivicsRepository(
        private val electionDao: ElectionDao,
        private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO): CivicsDataSource {

    override val electionsFollowed: LiveData<List<Election>> = Transformations.map(electionDao.getFollowedElections()) {
        it.toDomainModel()
    }

    override val electionsUpcoming: LiveData<List<Election>> = Transformations.map((electionDao.getAllElections())){
        it.toDomainModel()
    }


    override suspend fun refreshElectionsData() {
        withContext(Dispatchers.IO) {
            try {

                val response = CivicsApi.retrofitService.getElections()
                val elections = response.elections.toDomainModel()
                electionDao.insertAll(*elections.toDatabaseEntity())
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Timber.d("Refresh failed ${e.message}")
                }
                e.printStackTrace()
            }
        }
    }

    override suspend fun saveElection(election: ElectionEntity) = withContext(ioDispatcher){
        try{
            electionDao.updateElection(election)
        }
        catch (e: Exception){
            Timber.e("Error while saving the Election: ${e.message}")
        }

    }

    override fun getSavedElections(): LiveData<List<ElectionEntity>> = electionDao.getFollowedElections()

    override fun getElectionById(id: Int) = electionDao.getElectionById(id)

    override suspend fun deleteElection(election: ElectionEntity) = withContext(ioDispatcher){
        try {
            electionDao.deleteElection(election)
        }catch (e: Exception){
            Timber.e("Error while deleting the Election: ${e.message}")
        }
    }

    override suspend fun clear() = withContext(ioDispatcher){
        try {
            electionDao.clear()
        }catch (e: Exception){
            Timber.e("Error while clearing the Elections: ${e.message}")
        }
    }
}