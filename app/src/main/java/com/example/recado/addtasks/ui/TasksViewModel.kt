package com.example.recado.addtasks.ui


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recado.addtasks.domain.AddTaskUseCase
import com.example.recado.addtasks.domain.GetTaksUseCase
import com.example.recado.addtasks.ui.TasksUiState.*
import com.example.recado.addtasks.ui.model.TaskModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class TasksViewModel @Inject constructor(
    private val addTaskUseCase: AddTaskUseCase,
    getTasksUseCase: GetTaksUseCase
) : ViewModel() {

    val uiState: StateFlow<TasksUiState> = getTasksUseCase().map(::Success)
        .catch {Error(it)}
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), Loading)

    private val _showDialog = MutableLiveData<Boolean>()
    val showDialog: LiveData<Boolean> = _showDialog

//   private val _tasks = mutableStateListOf<TaskModel>()
//   val task: List<TaskModel> = _tasks

    fun onDialogClose() {
        _showDialog.value = false
    }

    fun onTaskCreated(task: String) {
        _showDialog.value = false
        viewModelScope.launch {
            addTaskUseCase(TaskModel(task = task))
        }
    }

    fun onShowDialogClick() {
        _showDialog.value = true
    }

    fun onCheckBoxSelected(taskModel: TaskModel) {
        // Este metodo es creando un LiveData, de otra forma JetPack no recompone la vista
        // Todo Check
//        val index = _tasks.indexOf(taskModel)
//        _tasks[index] = _tasks[index].let {
//            it.copy(selected = !it.selected)
//        }
    }

    fun onItemRemove(taskModel: TaskModel) {
        // Remove item
//        val task = _tasks.find { it.id == taskModel.id }
//        _tasks.remove(task)
    }
}