package com.example.android.politicalpreparedness.presentation.representative

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.content.PermissionChecker
import androidx.lifecycle.Observer
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.data.network.models.Address
import com.example.android.politicalpreparedness.databinding.FragmentRepresentativeBinding
import com.example.android.politicalpreparedness.presentation.base.BaseFragment
import com.example.android.politicalpreparedness.presentation.representative.adapter.RepresentativeListAdapter
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import org.koin.android.ext.android.inject
import timber.log.Timber
import java.util.*

class RepresentativeFragment : BaseFragment() {

    lateinit var binding : FragmentRepresentativeBinding
    override val _viewModel: RepresentativeViewModel by inject()

    // The entry point to the Fused Location Provider.
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    private var lastKnownLocation: Location? = null

    companion object {

        private const val REQUEST_CODE_LOCATION = 125
        private const val REQUEST_TURN_DEVICE_LOCATION_ON = 126

        private val LOCATION_PERMISSIONS = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_BACKGROUND_LOCATION
            )
        } else {
            arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION
            )
        }
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        //Establish bindings
        binding = FragmentRepresentativeBinding.inflate(inflater)

        // Construct a FusedLocationProviderClient.
        fusedLocationProviderClient =
                LocationServices.getFusedLocationProviderClient(requireActivity())

        binding.lifecycleOwner = this

        binding.apply {

            //Define and assign Representative adapter
            val adapter = RepresentativeListAdapter()
            rvRepresentatives.adapter = adapter

            //Populate Representative adapter
            _viewModel.representatives.observe(viewLifecycleOwner, Observer {
                adapter.submitList(it)
            })

            //Establish button listeners for field and location search
            buttonLocation.setOnClickListener {
                checkLocationPermissions()
            }

            buttonSearch.setOnClickListener {
                hideKeyboard()
                _viewModel.getAddressFromIndividualFields(Address(addressLine1.text.toString(),
                        addressLine2.text.toString(),
                        city.text.toString(),
                        state.selectedItem.toString(),
                        zip.text.toString()))
            }

            lifecycleOwner?.let { owner ->
                _viewModel.address.observe(owner, Observer {
                    Timber.i("$it")
                    _viewModel.getRepresentatives()
                })
            }
        }

        return binding.root

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when(requestCode){
            REQUEST_CODE_LOCATION ->{
                grantResults.forEach {
                    if(it != PackageManager.PERMISSION_GRANTED){
                        _viewModel.showSnackBar.postValue(getString(R.string.permission_denied_explanation))
                        return
                    }
                }

                getLocation()
            }
        }
    }

    private fun checkLocationPermissions() {
        if (isPermissionGranted()) {
            getLocation()
        } else {
            requestPermissions(LOCATION_PERMISSIONS, REQUEST_CODE_LOCATION)
        }
    }

    private fun isPermissionGranted() : Boolean {
        val foregroundLocationApproved = (
                PermissionChecker.PERMISSION_GRANTED ==
                        PermissionChecker.checkSelfPermission(
                                requireContext(),
                                Manifest.permission.ACCESS_FINE_LOCATION
                        ))
        val backgroundPermissionApproved =
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    PermissionChecker.PERMISSION_GRANTED ==
                            PermissionChecker.checkSelfPermission(
                                    requireContext(), Manifest.permission.ACCESS_BACKGROUND_LOCATION
                            )
                } else {
                    true
                }
        return foregroundLocationApproved && backgroundPermissionApproved
    }

    @SuppressLint("MissingPermission")
    private fun getLocation() {
        //TODO: Get location from LocationServices
        //TODO: The geoCodeLocation method is a helper function to change the lat/long location to a human readable street address
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
        try {
            if (isPermissionGranted()) {
                val locationResult = fusedLocationProviderClient.lastLocation
                locationResult.addOnCompleteListener(requireActivity()) { task ->

                    if (task.isSuccessful) {

                        // Set the map's camera position to the current location of the device.
                        lastKnownLocation = task.result
                        if (lastKnownLocation != null) {
                            Timber.d("last  known location $lastKnownLocation")

                            val address = geoCodeLocation(lastKnownLocation!!)
                            _viewModel.setAddressFromGeoLocation(address)
                            populateAddressViews(address)
                        } else {
                            _viewModel.showSnackBar.value = "Cannot find representatives at this location"
                        }
                    }
                }
            }
        } catch (e: Exception) {
            Timber.e("Exception: %s", e.message)
            _viewModel.showSnackBar.value = "Exception Occurred, ${e.localizedMessage}, Cannot find representatives at this location"
        }
    }

    private fun populateAddressViews(address: Address) {
        binding.apply {
            addressLine1.setText(address.line1)
            addressLine2.setText(address.line2)
            city.setText(address.city)
            val statesArray = resources.getStringArray(R.array.states)
            state.setSelection(statesArray.indexOf(address.state))
            zip.setText(address.zip)

        }
    }

    private fun geoCodeLocation(location: Location): Address {
        val geocoder = Geocoder(context, Locale.getDefault())
        return geocoder.getFromLocation(location.latitude, location.longitude, 1)
                .map { address ->
                    Address(address.thoroughfare, address.subThoroughfare, address.locality, address.adminArea, address.postalCode)
                }
                .first()
    }

    private fun hideKeyboard() {
        val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view!!.windowToken, 0)
    }

}