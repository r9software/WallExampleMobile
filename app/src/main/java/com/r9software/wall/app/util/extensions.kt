package com.r9software.wall.app.util

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import java.text.SimpleDateFormat

fun String?.formatDate(): String {
    if(this==null)
        return ""
    val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    var date = format.parse(this)
    val formatted = SimpleDateFormat("MMM dd, yyyy HH:mm")
    return formatted.format(date)
}
/**
 * Extension function to simplify setting an afterTextChanged action to EditText components.
 */
fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(editable: Editable?) {
            afterTextChanged.invoke(editable.toString())
        }

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
    })
}