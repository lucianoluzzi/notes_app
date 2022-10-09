package nl.com.lucianoluzzi.design.extensions

import androidx.appcompat.widget.SearchView

// search watcher
fun SearchView.doOnSearchAction(action: (String) -> Unit) {
    setOnQueryTextListener(object : SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(query: String) = false

        override fun onQueryTextChange(newText: String): Boolean {
            action.invoke(newText)
            return true
        }
    })
}
