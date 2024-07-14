package com.example.studybuddy.view.task

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.studybuddy.subjects
import com.example.studybuddy.ui.theme.Red
import com.example.studybuddy.utils.Priority
import com.example.studybuddy.utils.changeMillisToDateString
import com.example.studybuddy.view.components.DeleteDialog
import com.example.studybuddy.view.components.SubjectListDropDown
import com.example.studybuddy.view.components.TaskCheckBox
import com.example.studybuddy.view.components.TaskDatePicker
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.launch


data class TaskScreenNavArgs(
    val taskId:Int?,
    val subjectId:Int?
)


@Destination(navArgsDelegate = TaskScreenNavArgs::class)
@Composable
fun TaskScreenRoute(
    navigator:DestinationsNavigator
){
    TaskScreen(
        onBackButtonClick = {navigator.navigateUp()}
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TaskScreen(
    onBackButtonClick: () -> Unit
)
{

    val scope = rememberCoroutineScope()
    var isSubjectDropDownOpen by rememberSaveable { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()
    var isTaskDatePickerOpen by rememberSaveable { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState(initialSelectedDateMillis = System.currentTimeMillis())
    var isDeleteTaskDialogOpen by rememberSaveable { mutableStateOf(false) }
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var taskTitleError by remember{mutableStateOf<String?>(null)}
    var subjectTitle by remember { mutableStateOf("") }

    taskTitleError = when{
        title.isBlank() -> "Please enter task title"
        title.length > 30 -> "Task title cannot be more than 30 characters"
        title.length<4 -> "Task title cannot be less than 4 characters"

        else-> null
    }

    DeleteDialog(
        title = "Delete Task?",
        bodyText = "Are you sure you want to delete this task? "+
                " This action is irreversible",
        isOpen = isDeleteTaskDialogOpen ,
        onDismiss = {isDeleteTaskDialogOpen = false },
        onConfirmBtnClick = {isDeleteTaskDialogOpen = false})

    TaskDatePicker(
        state = datePickerState,
        isOpen =isTaskDatePickerOpen ,
        onDismissRequest = { isTaskDatePickerOpen=false},
        onConfirmRequest = { isTaskDatePickerOpen = false }
    )

    SubjectListDropDown(
        sheetState =sheetState ,
        isOpen = isSubjectDropDownOpen,
        subjects = subjects ,
        onDismissRequest = {isSubjectDropDownOpen=false},
        onSubjectClick ={
            isSubjectDropDownOpen = false
            subjectTitle = it.name

            scope.launch { sheetState.hide() }.invokeOnCompletion {
                if (!sheetState.isVisible) {
                    isSubjectDropDownOpen = false
                }
            }
        }
    )


    Scaffold(
    topBar = {
        TaskScreenTopBar(
            isTaskExist = true,
            isCompleted = false,
            checkBoxBorderColor = Red,
            onBackButtonClick = onBackButtonClick,
            onCheckBoxClick = {},
            onDeleteButtonClick = {isDeleteTaskDialogOpen = true}
        )
    }
) {paddingValues ->

    Column (modifier = Modifier
        .verticalScroll(rememberScrollState())
        .fillMaxSize()
        .padding(paddingValues)
        .padding(horizontal = 12.dp)
    ){
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = title,
            onValueChange = {title=it},
            label = { Text(text = "Title")},
            singleLine = true,
            isError = taskTitleError != null && title.isNotBlank(),
            supportingText = { Text(text = taskTitleError.orEmpty())}
        )


        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = description,
            onValueChange ={description=it},
            label = { Text(text = "Description")},
            )

        Spacer(modifier = Modifier.height(20.dp))
        
        Text(text = "Due Date",
            style = MaterialTheme.typography.bodySmall
        )
        Row(modifier = Modifier
            .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(text = datePickerState.selectedDateMillis.changeMillisToDateString(),
                style = MaterialTheme.typography.bodyLarge
            )
            IconButton(onClick = { isTaskDatePickerOpen = true }) {
                Icon(imageVector = Icons.Default.DateRange,
                    contentDescription ="Select Due Date" )
                
            }

        }

        Spacer(modifier = Modifier.height(10.dp))

        Text(text = "Priority",
            style = MaterialTheme.typography.bodySmall
        )

        Spacer(modifier = Modifier.height(10.dp))

        Row(modifier = Modifier.fillMaxWidth()) {
            Priority.entries.forEach{
                PriorityButton(
                    modifier = Modifier.weight(1f),
                    label = it.name,
                    backgroundColor = it.color,
                    borderColor = if (it == Priority.MEDIUM){
                        Color.White
                    } else Color.Transparent,
                    labelColor = if (it == Priority.MEDIUM){
                        Color.White
                    } else Color.White.copy(alpha = 0.7f),
                    onClick = {}
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(text = "Related to subject",
            style = MaterialTheme.typography.bodySmall
        )
        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = if (subjectTitle.isBlank()) "Select Subject" else subjectTitle,
                style = MaterialTheme.typography.bodyLarge
            )

            IconButton(onClick = { isSubjectDropDownOpen=true }) {
                Icon(imageVector = Icons.Default.KeyboardArrowDown,
                    contentDescription ="Select Subject" )

            }

        }

        Button(
            enabled = taskTitleError==null,
            onClick = { /*TODO*/ },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp)
        ) {
            Text(text = "Save")

        }



    }
}
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TaskScreenTopBar(
    isTaskExist: Boolean,
    isCompleted: Boolean,
    checkBoxBorderColor: Color,
    onBackButtonClick: () -> Unit,
    onDeleteButtonClick: () -> Unit,
    onCheckBoxClick: () -> Unit
)
{
    TopAppBar(
        navigationIcon = {
            IconButton(onClick = onBackButtonClick ) {
                Icon(imageVector = Icons.Default.ArrowBack,
                    contentDescription ="Navigate Back" )
            }
        },
        title = {
            Text(text = "Task")
        },
        actions = {
            if (isTaskExist)
            {
                TaskCheckBox(isCompleted = isCompleted,
                    borderColor = checkBoxBorderColor,
                    onCheckBoxClicked = onCheckBoxClick
                )
                IconButton(onClick = onDeleteButtonClick ) {
                    Icon(imageVector = Icons.Default.Delete,
                        contentDescription ="Delete Task" )
                }
            }
        }
    )
}


@Composable
private fun PriorityButton(
    modifier: Modifier = Modifier,
    label: String,
    backgroundColor: Color,
    borderColor: Color,
    labelColor: Color,
    onClick: () -> Unit
)
{
    Box(modifier = modifier
        .background(backgroundColor)
        .clickable { onClick() }
        .padding(5.dp)
        .border(1.dp, borderColor, RoundedCornerShape(5.dp))
        .padding(5.dp),
        contentAlignment = Alignment.Center
    ){
        Text(text = label,
            color = labelColor
        )
    }

}

