package br.com.devrafamenegon.projects.taskmanagement.models

import javax.persistence.Entity
import javax.persistence.*

@Entity
class User (
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    val name: String = "",
    val email: String = "",
    var password: String = ""
)