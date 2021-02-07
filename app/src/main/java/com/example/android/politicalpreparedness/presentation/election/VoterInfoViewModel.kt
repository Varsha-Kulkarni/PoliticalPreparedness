package com.example.android.politicalpreparedness.presentation.election

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.android.politicalpreparedness.data.CivicsDataSource
import com.example.android.politicalpreparedness.data.network.models.VoterInfoResponse
import com.example.android.politicalpreparedness.domain.models.Election
import com.example.android.politicalpreparedness.presentation.base.BaseViewModel
import kotlinx.coroutines.launch
import com.example.android.politicalpreparedness.domain.models.Result
import com.example.android.politicalpreparedness.domain.models.toDatabaseEntity
import timber.log.Timber

class VoterInfoViewModel(val app: Application, private val dataSource: CivicsDataSource) : BaseViewModel(app) {

    //live data to hold voter info
    private val _voterInfo = MutableLiveData<VoterInfoResponse>()
    val voterInfo : LiveData<VoterInfoResponse>
    get() = _voterInfo

    private var _election = MutableLiveData<Election>()
    val election: LiveData<Election>
        get() = _election

    fun getVoterInfo(){
        viewModelScope.launch {
            Timber.i("$election.id $election.division")
            when(val result= dataSource.getVoterInfo(election.value!!)){
                is Result.Success -> {
                    _voterInfo.postValue(result.data)
                }
                is Result.Error -> {
                    showSnackBar.postValue("Failed to Parse address, $result.data")
                }
            }
        }
    }

    fun setElection(election: Election){
        _election.value = election
    }

    /**
     * Hint: The saved state can be accomplished in multiple ways. It is directly related to how elections are saved/removed from the database.
     */
    fun updateElection(election: Election) {
        election.isFollowed = !election.isFollowed
        viewModelScope.launch {
            dataSource.updateElection(election.toDatabaseEntity())
            setElection(election)
        }
    }

}