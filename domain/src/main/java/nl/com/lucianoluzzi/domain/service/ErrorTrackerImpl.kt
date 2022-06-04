package nl.com.lucianoluzzi.domain.service

import android.util.Log

class ErrorTrackerImpl : ErrorTracker {
    override fun trackError(throwable: Throwable) {
        Log.e(ERROR_TAG, throwable.message ?: throwable.stackTraceToString())
    }

    private companion object {
        private const val ERROR_TAG = "ERROR"
    }
}
