package com.example.recado.addtasks.ui

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.recado.addtasks.ui.model.TaskModel
import kotlinx.coroutines.selects.select
import javax.inject.Inject


class TasksViewModel @Inject constructor():ViewModel() {

    private val _showDialog = MutableLiveData<Boolean>()
    val showDialog:LiveData<Boolean> = _showDialog

    private val _tasks = mutableStateListOf<TaskModel>()
    val task: List<TaskModel> = _tasks

    fun onDialogClose() {
        _showDialog.value = false    }

    fun onTaskCreated(task: String) {
        _showDialog.value = false
        _tasks.add(TaskModel(task))
    }

    fun onShowDialogClick() {
        _showDialog.value = true
    }

    fun onCheckBoxSelected(taskModel: TaskModel) {
        // Este metodo es creando un LiveData, de otra forma JetPack no recompone la vista
        val index = _tasks.indexOf(taskModel)
        _tasks[index] = _tasks[index].let {
            it.copy(selected = !it.selected)
        }
    }
}