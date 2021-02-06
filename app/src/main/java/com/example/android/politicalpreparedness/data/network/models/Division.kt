package com.example.android.politicalpreparedness.data.network.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Division(
        val id: String,
        val country: String,
        val state: String
) : Parcelable{

    fun toFormattedString() : String{
        var output = country.plus("\n")
        if(!state.isNullOrEmpty()){
            output = output.plus(state).plus("\n")
        }
        return output
    }
}