package nl.com.lucianoluzzi.domain.model

sealed class Response<out T> {
    data class Error(val errorMessage: String? = null) : Response<Nothing>()
    data class Success<out T>(val data: T? = null) : Response<T>()
}
