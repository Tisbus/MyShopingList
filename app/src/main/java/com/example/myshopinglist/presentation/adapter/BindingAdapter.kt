package com.example.myshopinglist.presentation.adapter

import androidx.databinding.BindingAdapter
import com.example.myshopinglist.R
import com.google.android.material.textfield.TextInputLayout

class BindingAdapter {
    @BindingAdapter("errorInputName")
    fun bindErrorInputName(textInputLayout: TextInputLayout, isError : Boolean) {
        val message = if (isError) {
            textInputLayout.context.getString(R.string.empty_string)
        } else {
            null
        }
        textInputLayout.error = message
    }
    @BindingAdapter("errorInputCount")
    fun bindErrorInputCount(textInputLayout: TextInputLayout, isError : Boolean) {
        val message = if (isError) {
            textInputLayout.context.getString(R.string.empty_string)
        } else {
            null
        }
        textInputLayout.error = message
    }
}