package com.example.studybuddy.view.subject

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.studybuddy.domain.model.Subject
import com.example.studybuddy.sessions
import com.example.studybuddy.taskitems
import com.example.studybuddy.view.components.AddSubjectDialog
import com.example.studybuddy.view.components.CountCards
import com.example.studybuddy.view.components.DeleteDialog
import com.example.studybuddy.view.components.StudySessionList
import com.example.studybuddy.view.components.TaskList
import com.example.studybuddy.view.destinations.TaskScreenRouteDestination
import com.example.studybuddy.view.task.TaskScreenNavArgs
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

data class SubjectScreenNavArgs(
    val subjectId:Int
)

@Destination(navArgsDelegate = SubjectScreenNavArgs::class)
@Composable
fun SubjectScreenRoute(
    navigator: DestinationsNavigator,
){
    SubjectScreen(
        onBackButtonClicked = { navigator.navigateUp() },
        onAddTaskButtonClick = {
            val navArgs = TaskScreenNavArgs(taskId = null, subjectId = null)
            navigator.navigate(TaskScreenRouteDestination(navArgs = navArgs))
                               },
        onTaskCardClick = { taskId ->
            taskId?.let{
                val navArgs = TaskScreenNavArgs(taskId, subjectId = null)
                navigator.navigate(TaskScreenRouteDestination(navArgs = navArgs))
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SubjectScreen(
    onBackButtonClicked: () -> Unit,
    onAddTaskButtonClick:() -> Unit,
    onTaskCardClick: (Int?) -> Unit
){


    var isEditSubjectDialogOpen by rememberSaveable { mutableStateOf(false) }


    var subjectName by remember {
        mutableStateOf("")
    }
    var goalHours by remember {
        mutableStateOf("")

    }
    var selectedColor by remember {
        mutableStateOf(Subject.subjectCardColor.random())
    }
    var isDeleteSubjectDialogOpen by rememberSaveable { mutableStateOf(false) }
    var isDeleteSessionDialogOpen by rememberSaveable { mutableStateOf(false) }



    AddSubjectDialog(
        subjectName = subjectName,
        goalHours = goalHours,
        onSubjectNameChange ={subjectName = it} ,
        onGoalHoursChange ={goalHours=it} ,
        selectedColor = selectedColor,
        onColourChange ={selectedColor=it} ,
        isOpen = isEditSubjectDialogOpen ,
        onDismiss = {isEditSubjectDialogOpen = false },
        onConfirmBtnClick = {isEditSubjectDialogOpen = false})



    DeleteDialog(
        title = "Delete Session?",
        bodyText = "Are you sure you want to delete this session? Your studied hour will be reduced"+
                " by the duration of the session. This action is irreversible",
        isOpen = isDeleteSessionDialogOpen ,
        onDismiss = {isDeleteSessionDialogOpen = false },
        onConfirmBtnClick = {isDeleteSessionDialogOpen = false})

    DeleteDialog(
        title = "Delete Subject?",
        bodyText = "Are you sure you want to delete this subject? All related"+
                " task and session will be permanently deleted. This action is irreversible",
        isOpen = isDeleteSubjectDialogOpen ,
        onDismiss = {isDeleteSubjectDialogOpen = false },
        onConfirmBtnClick = {isDeleteSubjectDialogOpen = false})

    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    val listState = rememberLazyListState()
    val isFloatingBtnExpanded by remember {
        derivedStateOf { listState.firstVisibleItemIndex==0 }
    }

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {SubjectScreenTopBar(
            title = "English",
            onBackButtonClicked = onBackButtonClicked,
            onDeleteButtonClicked = { isDeleteSubjectDialogOpen=true },
            onEditButtonClicked = { isEditSubjectDialogOpen=true },
            scrollBehavior = scrollBehavior
        )
                 },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = onAddTaskButtonClick,
                icon = { Icon(Icons.Default.Add, contentDescription = "Add Task") },
                text = { Text("Add Task")},
                expanded = isFloatingBtnExpanded
            )
        }
    ) {paddingValues ->

        LazyColumn (
            state = listState,
            modifier = Modifier
                .fillMaxWidth()
                .padding(paddingValues)){
            
            item { 
                SubjectOverviewSection(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    studiedHours = "2",
                    goalHours = "4",
                    progress = 0.50f
                )
            }
            TaskList(sectionTitle ="UPCOMING TASKS" ,
                Tasks = taskitems,
                emptyListText ="You don't have any task yet.\n Click + button to add new task.",
                onCheckBoxClick = {},
                onTaskCardClick = onTaskCardClick
            )
            item {
                Spacer(modifier = Modifier.height(20.dp))
            }

            TaskList(sectionTitle ="COMPLETED TASKS" ,
                Tasks = taskitems,
                emptyListText ="You don't have any completed task yet.\n"+
                        "Click the checkbox to complete task",
                onCheckBoxClick = {},
                onTaskCardClick = onTaskCardClick
            )
            item {
                Spacer(modifier = Modifier.height(20.dp))
            }

            StudySessionList(sectionTitle ="RECENT STUDY SESSION" ,
                Sessions = sessions ,
                emptyListText ="You don't have any recent study session.\n Start a study session to begin your progress.",
                onDeleteItemClick = {isDeleteSessionDialogOpen=true}
            )

            item {
                Spacer(modifier = Modifier.height(20.dp))
            }
        }

    }


}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubjectScreenTopBar(title:String,
                        onBackButtonClicked:()->Unit,
                        onDeleteButtonClicked:()->Unit,
                        onEditButtonClicked:()->Unit,
                        scrollBehavior:TopAppBarScrollBehavior) {
    LargeTopAppBar(
        scrollBehavior =scrollBehavior ,
        navigationIcon = {
            IconButton(onClick = onBackButtonClicked) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = "Back"
                )
            }
        },
        title = {
            Text(
                text = title,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.headlineSmall
            )
        },
        actions = {
            IconButton(onClick = onDeleteButtonClicked ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete Subject"
                )

            }
            IconButton(onClick = onEditButtonClicked) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Edit Subject"
                )
            }
        }


    )
}

@Composable
private fun SubjectOverviewSection(
    modifier: Modifier,
    studiedHours:String,
    goalHours:String,
    progress:Float
){
    val percentProgress = remember(progress) {
        (progress * 100).toInt().coerceIn(0,100)

    }
    Row(
        modifier= modifier,
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        CountCards(modifier = Modifier.weight(1f),
            headingText = "Goal Study Hours",
            count = goalHours)

        Spacer(modifier = Modifier.width(10.dp))

        CountCards(modifier = Modifier.weight(1f),
            headingText = "Study Hours",
            count = studiedHours)

        Spacer(modifier = Modifier.width(10.dp))

        Box(modifier = Modifier.size(75.dp),
            contentAlignment = Alignment.Center) {
            CircularProgressIndicator(modifier = Modifier
                .fillMaxSize(),
                progress = 1f,
                strokeWidth = 4.dp,
                strokeCap = StrokeCap.Round,
                color = MaterialTheme.colorScheme.surfaceVariant
            )
            CircularProgressIndicator(modifier = Modifier
                .fillMaxSize(),
                progress = progress,
                strokeWidth = 4.dp,
                strokeCap = StrokeCap.Round
            )
            
            Text(text = "$percentProgress%")
        }

    }
}




