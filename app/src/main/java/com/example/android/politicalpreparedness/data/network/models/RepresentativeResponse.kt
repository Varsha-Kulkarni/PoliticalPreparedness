package com.example.android.politicalpreparedness.data.network.models

import com.squareup.moshi.Json

data class RepresentativeResponse(
        val offices: List<Office>,
        val officials: List<Official>
)