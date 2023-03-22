package com.example.recado.addtasks.domain

import com.example.recado.addtasks.data.TaskRepository
import com.example.recado.addtasks.ui.model.TaskModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetTaksUseCase @Inject constructor(
    private val taskRepository: TaskRepository) {

    operator fun invoke(): Flow<List<TaskModel>> {
        return taskRepository.tasks
    }
}