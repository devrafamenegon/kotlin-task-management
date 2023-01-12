package br.com.devrafamenegon.projects.taskmanagement.repositories

import br.com.devrafamenegon.projects.taskmanagement.models.Task
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.time.LocalDate

@Repository
interface TaskRepository : JpaRepository<Task, Long> {

    @Query("SELECT t FROM Task t " +
            " WHERE t.user.id  = :idUser " +
            "   AND (:periodStart IS NULL OR t.previousEndDate >= :periodStart) " +
            "   AND (:periodEnd IS NULL OR t.previousEndDate <= :periodEnd) " +
            "   AND (:status = 0 OR (:status = 1 AND t.endDate IS NULL) " +
            "           OR (:status = 2 AND t.endDate IS NOT NULL)) ")
    fun findByUserWithFilter(idUser: Long, periodStart : LocalDate?, periodEnd : LocalDate?, status : Int) : List<Task>?
}