package com.example.android.politicalpreparedness.presentation.representative

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.android.politicalpreparedness.data.CivicsDataSource
import com.example.android.politicalpreparedness.data.network.models.Address
import com.example.android.politicalpreparedness.domain.models.Result
import com.example.android.politicalpreparedness.presentation.base.BaseViewModel
import com.example.android.politicalpreparedness.presentation.representative.model.Representative
import kotlinx.coroutines.launch


class RepresentativeViewModel(myApplication: Application, private val civicsDataSource: CivicsDataSource): BaseViewModel(myApplication){


    /**
     *  The following code will prove helpful in constructing a representative from the API. This code combines the two nodes of the RepresentativeResponse into a single official :

    val (offices, officials) = getRepresentativesDeferred.await()
    _representatives.value = offices.flatMap { office -> office.getRepresentatives(officials) }

    Note: getRepresentatives in the above code represents the method used to fetch data from the API
    Note: _representatives in the above code represents the established mutable live data housing representatives

     */

    //live data for representatives and address
    private val _representatives = MutableLiveData<List<Representative>>()
    val representatives : LiveData<List<Representative>>
        get() = _representatives

    private val _address = MutableLiveData<Address>()
    val address : LiveData<Address>
        get() = _address

    //function get address from geo location
    fun setAddressFromGeoLocation(address: Address){
        _address.value = address
    }

    //function to fetch representatives from API from a provided address
    fun getRepresentatives() {
        viewModelScope.launch {
            val result = civicsDataSource.getRepresentatives(address.value!!)
            when (result) {
                is Result.Success -> {
                    _representatives.postValue(result.data)

                }
                is Result.Error -> {
                    showSnackBar.value = "Error getting representatives"
                }
            }

        }
    }

    // function to get address from individual fields
    fun getAddressFromIndividualFields(address: Address){
        _address.value = address
    }

}
