package com.kleberson.finwise.model

import java.sql.Date

class Activity(val name: String, val category: String, val type: String, val price: Double, val date: Date ) {
    override fun toString(): String {
        return "Activity(name='$name', category='$category', type='$type', price=$price, date=$date)"
    }
}