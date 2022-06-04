package nl.com.lucianoluzzi.domain.service

interface ErrorTracker {
    fun trackError(throwable: Throwable)
}
