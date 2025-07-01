package com.kleberson.finwise.util

import java.util.Formatter

class FormatBalance {
    fun format(balance: Double): String {
        return Formatter().format("%.2f", balance).toString().replace('.', ',')
    }
}