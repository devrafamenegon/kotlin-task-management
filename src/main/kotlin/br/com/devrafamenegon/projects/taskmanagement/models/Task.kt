package br.com.devrafamenegon.projects.taskmanagement.models

import com.fasterxml.jackson.annotation.JsonBackReference
import java.time.LocalDate
import javax.persistence.*

@Entity
data class Task (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    var name: String = "",
    var previousEndDate: LocalDate = LocalDate.MIN,
    var endDate: LocalDate? = null,

    @JsonBackReference
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "idUser")
    val user: User? = null,
)
