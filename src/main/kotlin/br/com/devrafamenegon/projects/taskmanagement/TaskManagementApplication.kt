package br.com.devrafamenegon.projects.taskmanagement

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration
import org.springframework.boot.runApplication

@SpringBootApplication(exclude = [SecurityAutoConfiguration::class])
class TaskManagementApplication

fun main(args: Array<String>) {
	runApplication<TaskManagementApplication>(*args)
}
