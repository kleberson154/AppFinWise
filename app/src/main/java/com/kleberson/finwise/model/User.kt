package com.kleberson.finwise.model

class User(val id: Int, val name: String, val email: String, val contact: String, val password: String, val balance: Double = 0.0, val activities: List<Activity> = emptyList()) {
    override fun toString(): String {
        return "User(id=$id, name='$name', email='$email', contact='$contact', password='$password', balance=$balance, activities=$activities)"
    }
}