package com.example.studybuddy.view.session

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.studybuddy.sessions
import com.example.studybuddy.subjects
import com.example.studybuddy.view.components.DeleteDialog
import com.example.studybuddy.view.components.StudySessionList
import com.example.studybuddy.view.components.SubjectListDropDown
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.launch

@Destination
@Composable
fun SessionScreenRoute(
    navigator: DestinationsNavigator
)
{
    val viewModel:SessionScreenViewModel = hiltViewModel()

    SessionScreen(
        onBackButtonClick = { navigator.navigateUp()}
    )
}

@OptIn(ExperimentalMaterial3Api::class)

@Composable
private fun SessionScreen(
    onBackButtonClick: () -> Unit
)
{


    val scope = rememberCoroutineScope()
    var isSubjectDropDownOpen by rememberSaveable { mutableStateOf(false) }
    var subjectTitle by remember { mutableStateOf("") }
    val sheetState = rememberModalBottomSheetState()
    var isDeleteSessionDialogOpen by rememberSaveable { mutableStateOf(false) }



    DeleteDialog(
        title = "Delete Session?",
        bodyText = "Are you sure you want to delete this session? Your studied hour will be reduced"+
                " by the duration of the session. This action is irreversible",
        isOpen = isDeleteSessionDialogOpen ,
        onDismiss = {isDeleteSessionDialogOpen = false },
        onConfirmBtnClick = {isDeleteSessionDialogOpen = false})


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
            SessionScreenTopBar(
                onBackButtonClick = onBackButtonClick,

            )
        }
    ) {paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(paddingValues)
                .padding(horizontal = 12.dp)
        ) {
            item {
                TimerSection(modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f))

                Text(text = "Related to subject")
                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = subjectTitle.ifBlank { "Select Subject" },
                        style = MaterialTheme.typography.bodyLarge
                    )

                    IconButton(onClick = { isSubjectDropDownOpen=true }) {
                        Icon(imageVector = Icons.Default.KeyboardArrowDown,
                            contentDescription ="Select Subject" )

                    }

                }

                Spacer(modifier = Modifier.height(20.dp))
            }
            item { 
                TimerButtonSection(
                    modifier = Modifier.fillMaxWidth(),
                    onStartButtonClick = { /*TODO*/ },
                    onCancelButtonClick = { /*TODO*/ },
                    onFinishButtonClick = {}
                )
                Spacer(modifier = Modifier.height(20.dp))

            }

            StudySessionList(sectionTitle ="RECENT STUDY SESSION" ,
                Sessions = sessions ,
                emptyListText ="You don't have any recent study session.\n Start a study session to begin your progress.",
                onDeleteItemClick = {isDeleteSessionDialogOpen=true}
            )

        }

    }


}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SessionScreenTopBar(

    onBackButtonClick: () -> Unit,
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
            Text(text = "Session Screen")
        }
    )
}

@Composable
private fun TimerSection
            (
    modifier: Modifier
)
{
    Box(modifier = modifier,
        contentAlignment = Alignment.Center)
    {
        Box(
            modifier = Modifier
                .size(250.dp)
                .border(5.dp, MaterialTheme.colorScheme.surfaceVariant, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "00:02:43",
                style = MaterialTheme.typography.titleLarge.copy(fontSize = 45.sp))
        }
    }
}

@Composable
private fun TimerButtonSection(
    modifier: Modifier,
    onStartButtonClick: () -> Unit,
    onCancelButtonClick: () -> Unit,
    onFinishButtonClick: () -> Unit,
)
{
    Row(modifier = Modifier
        .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween) {
        Button(
            modifier = Modifier
                .padding(horizontal = 10.dp, vertical = 5.dp),
            onClick = { onCancelButtonClick() }) {
            Text(text = "Cancel")
        }

        Button(
            modifier = Modifier
                .padding(horizontal = 10.dp, vertical = 5.dp),
            onClick = { onStartButtonClick()}) {
            Text(text = "Start")
        }

        Button(
            modifier = Modifier
                .padding(horizontal = 10.dp, vertical = 5.dp),
            onClick = { onFinishButtonClick() }) {
            Text(text = "Finish")
        }
    }
}


