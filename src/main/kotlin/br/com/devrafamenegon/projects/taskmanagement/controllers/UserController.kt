package br.com.devrafamenegon.projects.taskmanagement.controllers

import br.com.devrafamenegon.projects.taskmanagement.models.User
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("api/user")
class UserController {

    @GetMapping
    fun getUser () = User(1, "Rafael", "menegon245@gmail.com", "")
}