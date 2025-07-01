package com.kleberson.finwise.model

import java.util.Date

data class Activity(val id: Int, val name: String, val category: String, val type: String, val price: Double, val date: Date) {
    override fun toString(): String {
        return "Activity(id=$id, name='$name', category='$category', type='$type', price=$price, date=$date)"
    }
}