package br.com.devrafamenegon.projects.taskmanagement.controllers

import br.com.devrafamenegon.projects.taskmanagement.dtos.ErrorDTO
import br.com.devrafamenegon.projects.taskmanagement.dtos.LoginDTO
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.lang.RuntimeException

@RestController
@RequestMapping("api/login")
class LoginController {
    @PostMapping
    fun login(@RequestBody dto : LoginDTO) : ResponseEntity<Any> {
        try {
            throw RuntimeException("Testing Exception")
        } catch (exception: Exception) {
            return ResponseEntity(
                ErrorDTO(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Failed to login, try again"),
                HttpStatus.INTERNAL_SERVER_ERROR
            )
        }
    }
}