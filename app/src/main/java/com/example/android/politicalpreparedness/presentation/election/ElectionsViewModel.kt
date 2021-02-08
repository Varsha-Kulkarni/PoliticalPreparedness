package com.example.android.politicalpreparedness.presentation.election

import android.app.Application
import androidx.lifecycle.viewModelScope
import com.example.android.politicalpreparedness.data.CivicsDataSource
import com.example.android.politicalpreparedness.presentation.base.BaseViewModel
import kotlinx.coroutines.launch

// ViewModel and provide election datasource
class ElectionsViewModel(myApplication: Application, civicsDataSource: CivicsDataSource): BaseViewModel(myApplication) {

    //val and functions to populate live data for upcoming elections from the API and followed elections from local database
    init{
        viewModelScope.launch{
            civicsDataSource.refreshElectionsData()
        }
    }

    // live data val for upcoming elections and followed elections
    val followedElections = civicsDataSource.electionsFollowed
    val upcomingElections = civicsDataSource.electionsUpcoming
}