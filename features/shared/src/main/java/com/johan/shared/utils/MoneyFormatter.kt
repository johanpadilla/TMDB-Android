package com.johan.shared.utils

import java.text.NumberFormat
import java.util.Locale

fun Int.toMoneyFormat(): String = NumberFormat.getNumberInstance(Locale.US).format(this)