package it.app.telehealth.ui.screens

import android.content.Context
import android.text.format.DateFormat
import it.app.telehealth.R
import kotlinx.datetime.Instant

/**
 * Return the value as string. Return "Unavailable" if null.
 */
fun valueOrUnavailable(value: String?, context: Context): String {
    if (value == null)
        return context.resources.getString(R.string.unavailable)
    return value
}

/**
 * Return the [Instant] value as string. Return "Unavailable" if null.
 */
fun valueOrUnavailable(value: Instant?, context: Context): String {
    if (value == null)
        return context.resources.getString(R.string.unavailable)

    val dateFormat = DateFormat.getDateFormat(context)
    return dateFormat.format(value.toEpochMilliseconds())
}