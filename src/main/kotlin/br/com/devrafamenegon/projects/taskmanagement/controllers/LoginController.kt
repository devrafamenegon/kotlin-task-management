package br.com.devrafamenegon.projects.taskmanagement.controllers

import br.com.devrafamenegon.projects.taskmanagement.dtos.ErrorDTO
import br.com.devrafamenegon.projects.taskmanagement.dtos.LoginDTO
import br.com.devrafamenegon.projects.taskmanagement.dtos.LoginResponseDTO
import br.com.devrafamenegon.projects.taskmanagement.extensions.md5
import br.com.devrafamenegon.projects.taskmanagement.extensions.toHex
import br.com.devrafamenegon.projects.taskmanagement.repositories.UserRepository
import br.com.devrafamenegon.projects.taskmanagement.utils.JWTUtils
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("api/login")
class LoginController (val userRepository : UserRepository) {
    @PostMapping
    fun login(@RequestBody dto : LoginDTO) : ResponseEntity<Any> {
        try {
            if (dto.login.isBlank() || dto.login.isEmpty() || dto.password.isBlank() || dto.password.isEmpty()) {
                return ResponseEntity(
                    ErrorDTO(HttpStatus.BAD_REQUEST.value(), "Invalid parameters"),
                    HttpStatus.BAD_REQUEST
                )
            }

            val user = userRepository.findByEmail(dto.login)

            if (user == null || user.password != md5(dto.password).toHex()) {
                return ResponseEntity(
                    ErrorDTO(HttpStatus.BAD_REQUEST.value(), "User or Password invalid"),
                    HttpStatus.BAD_REQUEST
                )
            }

            val token = JWTUtils().generateToken(user.id.toString())
            val userTest = LoginResponseDTO(user.name, user.email, token)

            return ResponseEntity(userTest, HttpStatus.OK)

        } catch (exception: Exception) {
            return ResponseEntity(
                ErrorDTO(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Failed to login, try again"),
                HttpStatus.INTERNAL_SERVER_ERROR
            )
        }
    }
}