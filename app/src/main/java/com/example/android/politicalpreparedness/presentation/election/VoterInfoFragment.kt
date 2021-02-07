package com.example.android.politicalpreparedness.presentation.election

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.data.network.models.VoterInfoResponse
import com.example.android.politicalpreparedness.databinding.FragmentVoterInfoBinding
import com.example.android.politicalpreparedness.presentation.base.BaseFragment
import org.koin.android.ext.android.inject

class VoterInfoFragment : BaseFragment() {

    override val _viewModel: VoterInfoViewModel by inject()

    private lateinit var binding: FragmentVoterInfoBinding
    private val args: VoterInfoFragmentArgs by navArgs()

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        binding = FragmentVoterInfoBinding.inflate(inflater)
        //binding values
        binding.lifecycleOwner = this

        val election = args.argElection
        binding.election = election

        _viewModel.setElection(election)

        _viewModel.getVoterInfo()

        _viewModel.election.observe(viewLifecycleOwner, Observer {
            binding.btnFollowElection.text = if(!election.isFollowed) getString(R.string.follow_election) else getString(R.string.unfollow_election)
        })

        //Populate voter info -- hide views without provided data.
        _viewModel.voterInfo.observe(viewLifecycleOwner, Observer {
            populateVoterInfo(it)
        })
        /**
        Hint: You will need to ensure proper data is provided from previous fragment.
         */

        binding.btnFollowElection.setOnClickListener {
            _viewModel.updateElection(election)
        }

        return binding.root

    }

    private fun populateVoterInfo(voterInfo: VoterInfoResponse) {
        val electionAdministrationBody = voterInfo.state?.firstOrNull()?.electionAdministrationBody

        binding.apply {
            stateLocations.setOnClickListener{
                openUrl( electionAdministrationBody?.votingLocationFinderUrl)
            }

            stateBallot.setOnClickListener{
                openUrl(electionAdministrationBody?.ballotInfoUrl)
            }

            address.text = electionAdministrationBody?.correspondenceAddress?.toFormattedString()
        }
    }

    //method to load URL intents
    private fun openUrl(url: String?) {
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
    }


}