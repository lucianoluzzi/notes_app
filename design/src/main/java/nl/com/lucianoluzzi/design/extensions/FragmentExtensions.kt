package nl.com.lucianoluzzi.design.extensions

import android.os.Handler
import android.os.Looper
import androidx.annotation.MainThread
import androidx.fragment.app.Fragment
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.viewbinding.ViewBinding
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty
import kotlin.reflect.full.functions

@MainThread
inline fun <reified T : ViewBinding> Fragment.viewBinding() =
    object : ReadOnlyProperty<Fragment, T> {
        private var binding: T? = null

        private val handler = Handler(Looper.getMainLooper())

        init {
            viewLifecycleOwnerLiveData.observe(
                this@viewBinding
            ) {
                it.lifecycle.addObserver(
                    object : DefaultLifecycleObserver {
                        override fun onDestroy(owner: LifecycleOwner) {
                            super.onDestroy(owner)
                            handler.post { binding = null }
                        }
                    }
                )
            }
        }

        override fun getValue(thisRef: Fragment, property: KProperty<*>): T {
            binding?.let { return it }
            val method = T::class.functions.find { it.name == "bind" }
                ?: throw IllegalStateException(
                    "Couldn't find the bind method of this view binding class"
                )
            return (method.call(requireView()) as T).also { binding = it }
        }
    }
