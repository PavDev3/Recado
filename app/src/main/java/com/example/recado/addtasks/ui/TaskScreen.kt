package com.example.recado.addtasks.ui


import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import com.example.recado.addtasks.ui.model.TaskModel


@Composable
fun TasksScreen(taskViewModel: TasksViewModel) {
    val showDialog: Boolean by taskViewModel.showDialog.observeAsState(false)
    val lifecycle = LocalLifecycleOwner.current.lifecycle

    val uiState by produceState<TasksUiState>(
        initialValue = TasksUiState.Loading,
        key1 = lifecycle, key2 = taskViewModel
    ) {
        lifecycle.repeatOnLifecycle(state = Lifecycle.State.STARTED) {
            taskViewModel.uiState.collect { value = it }
        }
    }

    when (uiState) {
        is TasksUiState.Error -> {}
        TasksUiState.Loading -> {
            CircularProgressIndicator()
        }
        is TasksUiState.Success -> {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(android.graphics.Color.parseColor("#1E0953")))
            ) {
                AddTaskDialog(
                    showDialog,
                    onDismiss = { taskViewModel.onDialogClose() },
                    onTaskAdded = { taskViewModel.onTaskCreated(it) })
                FabDialog(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(16.dp), taskViewModel
                )
                TaskList((uiState as TasksUiState.Success).tasks, taskViewModel)
            }
        }
    }
}

@Composable
fun TaskList(tasks: List<TaskModel>, taskViewModel: TasksViewModel) {

    LazyColumn {
        items(tasks, key = { it.id }) { task ->
            ItemTask(task, taskViewModel)
        }
    }
}

@Composable
fun ItemTask(taskModel: TaskModel, taskViewModel: TasksViewModel) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clip(shape = RoundedCornerShape(16.dp))
            .pointerInput(Unit) {
                detectTapGestures(onLongPress = {
                    taskViewModel.onItemRemove(taskModel)
                })
            }, elevation = 8.dp
    ) {
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = taskModel.task,
                modifier = Modifier
                    .weight(1f)
                    .padding(6.dp)

            )
            Checkbox(
                checked = taskModel.selected,
                onCheckedChange = { taskViewModel.onCheckBoxSelected(taskModel) },
                colors = CheckboxDefaults.colors(
                    checkedColor = Color(
                        android.graphics.Color.parseColor("#1E0953")
                    )
                )
            )
        }
    }
}

@Composable
fun FabDialog(modifier: Modifier, taskViewModel: TasksViewModel) {
    FloatingActionButton(onClick = {
        taskViewModel.onShowDialogClick()
    }, modifier = modifier, backgroundColor = Color.Black) {
        Icon(imageVector = Icons.Filled.Add, contentDescription = "", tint = Color.White)
    }
}

@Composable
fun AddTaskDialog(show: Boolean, onDismiss: () -> Unit, onTaskAdded: (String) -> Unit) {
    var myTask by remember { mutableStateOf("") }

    if (show) {
        Dialog(onDismissRequest = { onDismiss }) {
            Surface(
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth(), color = Color.White
            ) {
                Column(
                    Modifier
                        .fillMaxWidth()
                        .background(Color.White)
                        .padding(16.dp)
                ) {
                    Text(
                        text = "Añadir recado",
                        fontSize = 18.sp,
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        fontWeight = FontWeight.Bold,
                    )
                    Spacer(modifier = Modifier.size(16.dp))
                    TextField(
                        value = myTask,
                        onValueChange = { myTask = it },
                        singleLine = true,
                        maxLines = 1
                    )
                    Spacer(modifier = Modifier.size(16.dp))
                    Button(onClick = {
                        onTaskAdded(myTask)
                        myTask = ""
                    }, modifier = Modifier.fillMaxWidth()) {
                        Text(text = "Añadir recado")
                    }
                }
            }
        }
    }
}
