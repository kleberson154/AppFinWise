package com.kleberson.finwise.model

class Activity(val name: String, val category: String, val type: String, val price: Double ) {
    override fun toString(): String {
        return "Activity(name='$name', category='$category', type='$type', price=$price)"
    }
}