package com.example.android.politicalpreparedness.presentation.election

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.android.politicalpreparedness.databinding.FragmentElectionBinding
import com.example.android.politicalpreparedness.domain.models.Election
import com.example.android.politicalpreparedness.presentation.base.BaseFragment
import com.example.android.politicalpreparedness.presentation.base.NavigationCommand
import com.example.android.politicalpreparedness.presentation.election.adapter.ElectionListAdapter
import org.koin.android.ext.android.inject

class ElectionsFragment:  BaseFragment() {

    override val _viewModel: ElectionsViewModel by inject()

    private lateinit var binding: FragmentElectionBinding

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        //TODO: Link elections to voter info

        binding = FragmentElectionBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.electionViewModel = _viewModel
        binding.rvUpcoming.adapter = ElectionListAdapter(ElectionListAdapter.ElectionListener{
            navigateToVoterInfo(it)
        })
        binding.rvFollowed.adapter = ElectionListAdapter(ElectionListAdapter.ElectionListener{
            navigateToVoterInfo(it)
        })

        return binding.root

    }

    fun navigateToVoterInfo(election: Election){
        _viewModel.navigationCommand.postValue(NavigationCommand.To(ElectionsFragmentDirections.actionElectionsFragmentToVoterInfoFragment(election.id, election.division)))
    }
}