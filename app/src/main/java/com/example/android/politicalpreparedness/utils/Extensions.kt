package com.example.android.politicalpreparedness.utils

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.view.View


/**
 * Created By Varsha Kulkarni on 02/02/21
 */
fun View.fadeIn(){
    this.visibility = View.VISIBLE
    this.alpha = 0f
    this.animate().alpha(1f).setListener(object : AnimatorListenerAdapter(){
        override fun onAnimationEnd(animation: Animator?) {
            this@fadeIn.alpha = 1f
        }
    })
}

fun View.fadeOut(){
    this.visibility = View.GONE
    this.alpha = 1f
    this.animate().alpha(0f).setListener(object : AnimatorListenerAdapter(){
        override fun onAnimationEnd(animation: Animator?) {
            this@fadeOut.alpha = 0f
        }
    })
}