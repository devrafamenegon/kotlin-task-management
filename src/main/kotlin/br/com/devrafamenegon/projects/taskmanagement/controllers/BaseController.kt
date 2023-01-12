package br.com.devrafamenegon.projects.taskmanagement.controllers

import br.com.devrafamenegon.projects.taskmanagement.models.User
import br.com.devrafamenegon.projects.taskmanagement.repositories.UserRepository
import br.com.devrafamenegon.projects.taskmanagement.utils.JWTUtils
import org.springframework.data.repository.findByIdOrNull
import java.lang.IllegalArgumentException

open class BaseController (val userRepository: UserRepository) {
    fun readToken(auth: String): User {
        val token = auth.substring(7)
        var userIdString = JWTUtils().getUserId(token)

        if (userIdString == null || userIdString.isNullOrEmpty() || userIdString.isBlank()) {
            throw IllegalArgumentException("You don't hava access to this resource")
        }

        var user = userRepository.findByIdOrNull(userIdString.toLong())

        if (user == null) {
            throw IllegalArgumentException("User not found")
        }

        return user
    }
}