package nl.com.lucianoluzzi.design.extensions

import androidx.core.widget.doOnTextChanged
import com.google.android.material.textfield.TextInputEditText

fun TextInputEditText.doIfTextChanged(action: (String) -> Unit) {
    doOnTextChanged { text, _, before, count ->
        if (before != count) {
            action(text.toString())
        }
    }
}

fun TextInputEditText.getTextOrNull(): String? {
    return if (!text.isNullOrEmpty()) {
        text.toString()
    } else {
        null
    }
}
