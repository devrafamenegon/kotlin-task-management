package br.com.devrafamenegon.projects.taskmanagement.repositories

import br.com.devrafamenegon.projects.taskmanagement.models.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : JpaRepository<User, Long> {
    fun findByEmail(email: String) : User?
}