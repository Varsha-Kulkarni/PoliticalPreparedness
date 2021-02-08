package com.example.android.politicalpreparedness.utils

import android.view.View
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Spinner
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.domain.models.Election
import com.example.android.politicalpreparedness.presentation.election.adapter.ElectionListAdapter
import com.example.android.politicalpreparedness.presentation.representative.adapter.RepresentativeListAdapter
import com.example.android.politicalpreparedness.presentation.representative.model.Representative
import timber.log.Timber

@BindingAdapter("fadeVisible")
fun setFadeVisible(view: View, visible: Boolean? = false){
    if(view.tag == null){
        view.tag = true
        view.visibility = if(visible == true) View.VISIBLE  else View.GONE
    }
    else{
        view.animate().cancel()
        if(visible == true) {
            if(view.visibility == View.GONE)
                view.fadeIn()
        }
        else {
            if(view.visibility == View.VISIBLE)
                view.fadeOut()
        }
    }

}

@BindingAdapter("profileImage")
fun fetchImage(view: ImageView, src: String?) {
    src?.let {
        val uri = src.toUri().buildUpon().scheme("https").build()
        //TODO: Add Glide call to load image and circle crop - user ic_profile as a placeholder and for errors.
        Glide.with(view.context).load(uri).placeholder(R.drawable.ic_profile).transform(CircleCrop()).into(view)
    }
}

@BindingAdapter("stateValue")
fun Spinner.setNewValue(value: String?) {
    val adapter = toTypedAdapter<String>(this.adapter as ArrayAdapter<*>)
    val position = when (adapter.getItem(0)) {
        is String -> adapter.getPosition(value)
        else -> this.selectedItemPosition
    }
    if (position >= 0) {
        setSelection(position)
    }
}

inline fun <reified T> toTypedAdapter(adapter: ArrayAdapter<*>): ArrayAdapter<T>{
    return adapter as ArrayAdapter<T>
}

@BindingAdapter("followedElectionsList")
fun bindFollowedElectionList(recyclerView: RecyclerView, followedElectionList: List<Election>?){
    val adapter: ElectionListAdapter = recyclerView.adapter as ElectionListAdapter
    adapter.submitList(followedElectionList)
}

@BindingAdapter("upcomingElectionList")
fun bindUpcomingElectionList(recyclerView: RecyclerView, upcomingElectionList: List<Election>?){
    val adapter: ElectionListAdapter = recyclerView.adapter as ElectionListAdapter
    adapter.submitList(upcomingElectionList)
}

@BindingAdapter("representativesList")
fun bindRepresentativesList(recyclerView: RecyclerView, representativesList: List<Representative>?){
    val adapter: RepresentativeListAdapter = recyclerView.adapter as RepresentativeListAdapter
    Timber.i("$representativesList")
    adapter.submitList(representativesList)

}
