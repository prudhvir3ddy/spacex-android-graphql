package com.prudhvireddy.spacex.presentation.launches.view

import android.annotation.SuppressLint
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.prudhvireddy.spacex.R
import java.text.SimpleDateFormat
import java.util.Date


@SuppressLint("SimpleDateFormat")
@BindingAdapter("formattedDate")
fun formattedDateFrom(textView: TextView, date: String) {
    val dateNew: Date? = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(date)
    val output = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    textView.text = dateNew?.let {
        val formattedTime: String = output.format(it)
        String.format(textView.context.getString(R.string.launch_date_template), formattedTime)
    } ?: String.format(textView.context.getString(R.string.launch_date_template), "not available")

}