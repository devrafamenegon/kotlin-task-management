package br.com.devrafamenegon.projects.taskmanagement.controllers

import br.com.devrafamenegon.projects.taskmanagement.dtos.ErrorDTO
import br.com.devrafamenegon.projects.taskmanagement.dtos.SuccessDTO
import br.com.devrafamenegon.projects.taskmanagement.models.Task
import br.com.devrafamenegon.projects.taskmanagement.repositories.TaskRepository
import br.com.devrafamenegon.projects.taskmanagement.repositories.UserRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.time.LocalDate
import java.util.*

@RestController
@RequestMapping("/api/task")
class TaskController (userRepository: UserRepository,
    val taskRepository: TaskRepository
) : BaseController(userRepository) {

    @GetMapping
    fun getTasksByUser(
        @RequestHeader("Authorization") authorization: String,
        @RequestParam periodStart : Optional<String>,
        @RequestParam periodEnd : Optional<String>,
        @RequestParam status : Optional<Int>
    ) : ResponseEntity<Any> {

        try {
            val user = readToken(authorization)

            val periodStartDate = if (periodStart.isPresent() && periodStart.get().isNotEmpty()) {
                LocalDate.parse(periodStart.get())
            } else {
                null
            }

            val periodEndDate = if (periodEnd.isPresent() && periodEnd.get().isNotEmpty()) {
                LocalDate.parse(periodEnd.get())
            } else {
                null
            }

            val statusInt = if (status.isPresent()) {
                status.get()
            } else {
                0
            }

            val result = taskRepository.findByUserWithFilter(user.id, periodStartDate, periodEndDate, statusInt)

            return ResponseEntity(result, HttpStatus.OK)
        } catch (exception: Exception) {
            return ResponseEntity(ErrorDTO(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Can't list activities of this user"
            ), HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }

    @PostMapping
    fun addTask(@RequestBody req: Task, @RequestHeader("Authorization") authorization: String) : ResponseEntity<Any> {
        try {
            val user = readToken(authorization)

            var errors = mutableListOf<String>()

            if (req === null) {
                errors.add("Task not find")
            } else {
                if (req.name.isEmpty() || req.name.isBlank() || req.name.length < 4) {
                    errors.add("Invalid name")
                }

                if (req.previousEndDate.isBefore(LocalDate.now())) {
                    errors.add("Previous end date can't be smaller than today")
                }
            }

            if (errors.size > 0) {
                return ResponseEntity(ErrorDTO(HttpStatus.BAD_REQUEST.value(), errors =  errors), HttpStatus.BAD_REQUEST)
            }

            val taskToAdd = Task(
                name = req.name,
                previousEndDate = req.previousEndDate,
                user = user
            )

            taskRepository.save(taskToAdd)

            return ResponseEntity(SuccessDTO("Task add successfully"), HttpStatus.OK)
        } catch (exception: Exception) {
            return ResponseEntity(
                ErrorDTO(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Can't add task, try again"),
                HttpStatus.INTERNAL_SERVER_ERROR
            )
        }
    }

    @DeleteMapping("/{id}")
    fun deleteTask(
        @PathVariable id: Long,
        @RequestHeader("Authorization") authorization: String
    ) : ResponseEntity<Any> {
        try {
            val user = readToken(authorization)
            val task = taskRepository.findByIdOrNull(id)

            if (task == null || task.user?.id != user.id) {
                return ResponseEntity(
                    ErrorDTO(HttpStatus.BAD_REQUEST.value(), "Task not exists"),
                    HttpStatus.BAD_REQUEST
                )
            }

            taskRepository.delete(task)

            return ResponseEntity(SuccessDTO("Task delete successfully"), HttpStatus.OK)
        } catch (exception: Exception) {
            return ResponseEntity(
                ErrorDTO(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Can't delete task, try again"),
                HttpStatus.INTERNAL_SERVER_ERROR
            )
        }
    }

    @PutMapping("/{id}")
    fun updateTask(
        @PathVariable id: Long,
        @RequestBody updateModel: Task,
        @RequestHeader("Authorization") authorization: String
    ) : ResponseEntity<Any> {
        try {
            val user = readToken(authorization)
            val task = taskRepository.findByIdOrNull(id)

            var errors = mutableListOf<String>()

            if (user == null || task == null) {
                return ResponseEntity(
                    ErrorDTO(HttpStatus.BAD_REQUEST.value(), "Task not exists"),
                    HttpStatus.BAD_REQUEST
                )
            }

            if (updateModel == null) {
                errors.add("Please, send the data you wish to update")
            } else {
                if (!updateModel.name.isNullOrBlank() && !updateModel.name.isNullOrEmpty() && updateModel.name.length < 4) {
                    errors.add("Invalid name")
                }

                if (updateModel.endDate != null && updateModel.endDate == LocalDate.MIN) {
                    errors.add("Invalid end date")
                }
            }

            if (errors.size > 0) {
                return ResponseEntity(ErrorDTO(HttpStatus.BAD_REQUEST.value(), errors =  errors), HttpStatus.BAD_REQUEST)
            }

            if (!updateModel.name.isNullOrEmpty() && !updateModel.name.isNullOrBlank()) {
                task.name = updateModel.name
            }

            if (!updateModel.previousEndDate.isBefore(LocalDate.now())) {
                task.previousEndDate = updateModel.previousEndDate
            }

            if (updateModel.endDate != null && updateModel.endDate != LocalDate.MIN) {
                task.endDate = updateModel.endDate
            }

            taskRepository.save(task)

            return ResponseEntity(SuccessDTO("Task update successfully"), HttpStatus.OK)
        } catch (exception: Exception) {
            return ResponseEntity(
                ErrorDTO(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Can't delete task, try again"),
                HttpStatus.INTERNAL_SERVER_ERROR
            )
        }
    }

}