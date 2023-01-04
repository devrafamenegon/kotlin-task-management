package br.com.devrafamenegon.projects.taskmanagement

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class TaskManagementApplication

fun main(args: Array<String>) {
	runApplication<TaskManagementApplication>(*args)
}
