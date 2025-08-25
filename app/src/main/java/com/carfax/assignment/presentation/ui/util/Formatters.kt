package com.carfax.assignment.presentation.ui.util

import kotlin.math.roundToInt

fun Double?.asUsd(): String = this?.let { "$" + "%,d".format(it.roundToInt()) } ?: "—"

fun Long?.asMileage(): String = this?.let {
    if (it >= 1000) {
        val oneDecimal = ((it / 1000.0 * 10).roundToInt() / 10.0)
        "${oneDecimal}k mi"
    } else "%,d mi".format(it)
} ?: "—"

fun cityState(city: String?, state: String?): String =
    listOfNotNull(city, state).joinToString(", ").ifEmpty { "—" }