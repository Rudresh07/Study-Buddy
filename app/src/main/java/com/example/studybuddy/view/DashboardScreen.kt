package com.example.studybuddy.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.studybuddy.R
import com.example.studybuddy.domain.model.Subject
import com.example.studybuddy.sessions
import com.example.studybuddy.subjects
import com.example.studybuddy.taskitems
import com.example.studybuddy.view.components.AddSubjectDialog
import com.example.studybuddy.view.components.CountCards
import com.example.studybuddy.view.components.DeleteDialog
import com.example.studybuddy.view.components.StudySessionList
import com.example.studybuddy.view.components.SubjectCard
import com.example.studybuddy.view.components.TaskList
import com.example.studybuddy.view.destinations.SessionScreenRouteDestination
import com.example.studybuddy.view.destinations.SubjectScreenRouteDestination
import com.example.studybuddy.view.destinations.TaskScreenRouteDestination
import com.example.studybuddy.view.subject.SubjectScreenNavArgs
import com.example.studybuddy.view.task.TaskScreenNavArgs
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Destination(start = true)
@Composable
fun DashBoardScreenRoute(
    navigator: DestinationsNavigator
){
    val viewModel : DashboardViewModel = hiltViewModel()

    DashboardScreen(
        onTaskCardClick ={taskId ->
                         taskId?.let{
                             val navArgs = TaskScreenNavArgs(taskId, subjectId = null)
                             navigator.navigate(TaskScreenRouteDestination(navArgs = navArgs))
                         }
        } ,
        onSubjectCardClick ={
                subjectId ->
            subjectId?.let {
                val navArgs = SubjectScreenNavArgs(subjectId)
                navigator.navigate(SubjectScreenRouteDestination(navArgs))
            }
        } ,
        onStartSessionButtonClick ={
            navigator.navigate(SessionScreenRouteDestination())
        } )
}


@Composable
private fun DashboardScreen(
    onTaskCardClick: (Int?) -> Unit,
    onSubjectCardClick: (Int?) -> Unit,
    onStartSessionButtonClick: () -> Unit,
){


    var isAddSubjectDialogOpen by rememberSaveable { mutableStateOf(false) }


    var subjectName by remember {
        mutableStateOf("")
    }
    var goalHours by remember {
        mutableStateOf("")

    }
    var selectedColor by remember {
        mutableStateOf(Subject.subjectCardColor.random())
    }



    AddSubjectDialog(
        subjectName = subjectName,
        goalHours = goalHours,
        onSubjectNameChange ={subjectName = it} ,
        onGoalHoursChange ={goalHours=it} ,
        selectedColor = selectedColor,
        onColourChange ={selectedColor=it} ,
        isOpen = isAddSubjectDialogOpen ,
        onDismiss = {isAddSubjectDialogOpen = false },
        onConfirmBtnClick = {isAddSubjectDialogOpen = false})

    var isDeleteDialogOpen by rememberSaveable { mutableStateOf(false) }

    DeleteDialog(
        title = "Delete Session?",
        bodyText = "Are you sure you want to delete this session? Your studied hour will be reduced"+
        " by the duration of the session. This action is irreversible",
        isOpen = isDeleteDialogOpen ,
        onDismiss = {isDeleteDialogOpen = false },
        onConfirmBtnClick = {isDeleteDialogOpen = false})


    Scaffold (
        topBar = {DashboardTopBar()}
    ){paddingValues ->
        LazyColumn (modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)){
            item {
                CountCardSection(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    subjectCount =5 ,
                    studyHours ="10" ,
                    targetHours = "12")
            }
            item{
                SubjectCardSection(
                    modifier = Modifier.fillMaxWidth(),
                    subjectList = subjects,
                    onAddIconClick = {isAddSubjectDialogOpen = true},
                    onSubjectCardClick = onSubjectCardClick
                )
            }

            item {
                Button(onClick = onStartSessionButtonClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 48.dp, vertical = 20.dp)
                ) {
                    Text(text = "Start Study Session")
                }
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

            StudySessionList(sectionTitle ="RECENT STUDY SESSION" ,
                Sessions = sessions ,
                emptyListText ="You don't have any recent study session.\n Start a study session to begin your progress.",
                onDeleteItemClick = {isDeleteDialogOpen=true}
            )

            item {
                Spacer(modifier = Modifier.height(20.dp))
            }



        }



    }

}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardTopBar(){
    CenterAlignedTopAppBar(title = {
        Text(text = "StudyBuddy",
            style = MaterialTheme.typography.headlineMedium)
    })
}

@Composable
private fun CountCardSection(
    modifier: Modifier,
    subjectCount: Int,
    studyHours: String,
    targetHours: String
){
    Row {
        CountCards(
            modifier = modifier.weight(1f),
            headingText = "Subject Count",
            count = "$subjectCount"
        )

        Spacer(modifier = Modifier.width(10.dp))

        CountCards(
            modifier = modifier.weight(1f),
            headingText = "Study Hours",
            count = studyHours
        )

        Spacer(modifier = Modifier.width(10.dp))

        CountCards(
            modifier = modifier.weight(1f),
            headingText = "Target Hours",
            count = targetHours
        )


    }
}

@Composable
private fun SubjectCardSection(
    modifier: Modifier,
    subjectList:List<Subject>,
    emptyListText:String = "You don't have any subject yet.\n Click + button to add new subject." ,
    onAddIconClick: () -> Unit,
    onSubjectCardClick: (Int?) -> Unit
){
    Column(modifier = modifier) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "SUBJECTS",
                modifier = Modifier.padding(12.dp),
                style = MaterialTheme.typography.bodySmall
            )

            IconButton(onClick = onAddIconClick ) {
                Icon(imageVector = Icons.Default.Add,
                    contentDescription ="Add Subjects" )

            }

        }
        if (subjectList.isEmpty()){
            Image(
                modifier = Modifier
                    .size(120.dp)
                    .align(Alignment.CenterHorizontally) ,
                painter = painterResource(id = R.drawable.books),
                contentDescription =emptyListText )
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = emptyListText,
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray,
                textAlign = TextAlign.Center
            )
        }

        LazyRow (
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(start = 12.dp, end = 12.dp)
        ){
            items(subjectList){subject ->
                SubjectCard(
                    subjectName = subject.name,
                    gradientColors =subject.color.map { Color(it) },
                    onClick = {onSubjectCardClick(subject.SubjectId)})

                }

            }
        }
    }
