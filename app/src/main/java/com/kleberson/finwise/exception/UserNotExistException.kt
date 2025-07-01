package com.kleberson.finwise.exception

class UserNotExistException(
    message: String = "Usuário não encontrado",
) : Exception(message) {
    override val message: String
        get() = "Error: ${super.message}"
}