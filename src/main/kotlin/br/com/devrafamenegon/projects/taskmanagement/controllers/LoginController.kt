package br.com.devrafamenegon.projects.taskmanagement.controllers

import br.com.devrafamenegon.projects.taskmanagement.dtos.ErrorDTO
import br.com.devrafamenegon.projects.taskmanagement.dtos.LoginDTO
import br.com.devrafamenegon.projects.taskmanagement.dtos.LoginResponseDTO
import br.com.devrafamenegon.projects.taskmanagement.utils.JWTUtils
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("api/login")
class LoginController {
    private val LOGIN_TEST = "admin@admin.com"
    private val PASSWORD_TEST = "admin123"

    @PostMapping
    fun login(@RequestBody dto : LoginDTO) : ResponseEntity<Any> {
        try {
            if (dto.login.isBlank() || dto.login.isEmpty() || dto.password.isBlank() || dto.password.isEmpty() || dto.login != LOGIN_TEST || dto.password != PASSWORD_TEST) {
                return ResponseEntity(
                    ErrorDTO(HttpStatus.BAD_REQUEST.value(), "Invalid parameters"),
                    HttpStatus.BAD_REQUEST
                )
            }

            val userId = 1
            val token = JWTUtils().generateToken(userId.toString())

            val userTest = LoginResponseDTO("User Test", LOGIN_TEST, token)


            return ResponseEntity(userTest, HttpStatus.OK)

        } catch (exception: Exception) {
            return ResponseEntity(
                ErrorDTO(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Failed to login, try again"),
                HttpStatus.INTERNAL_SERVER_ERROR
            )
        }
    }
}