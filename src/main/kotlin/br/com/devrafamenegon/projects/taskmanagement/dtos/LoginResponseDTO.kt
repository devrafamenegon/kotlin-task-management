package br.com.devrafamenegon.projects.taskmanagement.dtos

data class LoginResponseDTO (val name: String, val email: String, val token: String = "")