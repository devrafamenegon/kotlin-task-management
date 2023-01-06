package br.com.devrafamenegon.projects.taskmanagement.controllers

import br.com.devrafamenegon.projects.taskmanagement.dtos.ErrorDTO
import br.com.devrafamenegon.projects.taskmanagement.dtos.SuccessDTO
import br.com.devrafamenegon.projects.taskmanagement.models.User
import br.com.devrafamenegon.projects.taskmanagement.repositories.UserRepository
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("api/user")
class UserController (val userRepository : UserRepository) {

    @PostMapping
    fun createUser (@RequestBody user: User): ResponseEntity<Any> {
        try {
            val errors = mutableListOf<String>()

            if (user == null) {
                return ResponseEntity(
                    ErrorDTO(HttpStatus.BAD_REQUEST.value(), "Entry parameters not send"),
                    HttpStatus.BAD_REQUEST
                )
            }

            if (user.name.isEmpty() || user.name.isBlank() || user.name.length < 2) {
                errors.add("User name invalid")
            }

            if (user.email.isEmpty() || user.email.isBlank() || user.email.length < 5) {
                errors.add("Email invalid")
            }

            if (user.password.isEmpty() || user.password.isBlank() || user.password.length < 4) {
                errors.add("Password invalid")
            }

            if (errors.size > 0) {
                return ResponseEntity(ErrorDTO(HttpStatus.BAD_REQUEST.value(), null, errors), HttpStatus.BAD_REQUEST)
            }

            userRepository.save(user)

            return ResponseEntity(SuccessDTO("User created with success"), HttpStatus.OK)
        } catch (exception: Exception) {
            return ResponseEntity(
                ErrorDTO(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Failed to register, try again"),
                HttpStatus.INTERNAL_SERVER_ERROR
            )
        }
    }
}