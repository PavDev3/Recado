package com.example.recado.addtasks.ui.model

data class TaskModel(
    val task: String,
    var selected: Boolean = false,
    val id:Long = System.currentTimeMillis()
)
