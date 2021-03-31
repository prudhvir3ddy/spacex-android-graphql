package com.prudhvireddy.spacex.presentation.launchpads.view

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import androidx.databinding.BindingAdapter
import com.google.android.material.button.MaterialButton


@BindingAdapter("link")
fun handleLink(button: MaterialButton, url: String) {
    button.setOnClickListener {
        val webpage: Uri = Uri.parse(url)
        val intent = Intent(Intent.ACTION_VIEW, webpage)
        try {
            button.context.startActivity(intent)
        } catch (e: ActivityNotFoundException) {

        }
    }
}
