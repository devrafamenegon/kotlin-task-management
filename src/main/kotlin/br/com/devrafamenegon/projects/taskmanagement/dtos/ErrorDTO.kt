package br.com.devrafamenegon.projects.taskmanagement.dtos

class ErrorDTO (val status: Int, val error: String? = null, val errors : List<String>? = null)