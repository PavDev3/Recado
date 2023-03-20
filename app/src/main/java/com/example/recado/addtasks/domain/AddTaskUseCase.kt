package com.example.recado.addtasks.domain

import com.example.recado.addtasks.data.TaskRepository
import com.example.recado.addtasks.ui.model.TaskModel
import javax.inject.Inject

class AddTaskUseCase @Inject constructor(
    private val taskRepository: TaskRepository) {

    suspend operator fun invoke(taskModel: TaskModel){
        taskRepository.add(taskModel)

    }
}