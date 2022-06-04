package nl.com.lucianoluzzi.domain.useCase

import java.text.SimpleDateFormat
import java.util.*

class FormatDateUseCase(private val locale: Locale) {
    operator fun invoke(date: Date): String {
        return SimpleDateFormat(DATE_FORMAT, locale).format(date)
    }

    private companion object {
        private const val DATE_FORMAT = "dd/MM/yyyy"
    }
}
