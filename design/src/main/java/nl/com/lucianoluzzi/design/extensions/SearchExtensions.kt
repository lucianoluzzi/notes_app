package nl.com.lucianoluzzi.design.extensions

import androidx.appcompat.widget.SearchView

// search watcher
fun SearchView.doOnSearchAction(action: (String) -> Unit) {
    setOnQueryTextListener(object : SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(query: String): Boolean {
            action.invoke(query)
            return true
        }

        override fun onQueryTextChange(newText: String) = false
    })
}
