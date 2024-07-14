package com.example.studybuddy

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.studybuddy.domain.model.Session
import com.example.studybuddy.domain.model.Subject
import com.example.studybuddy.domain.model.Task
import com.example.studybuddy.ui.theme.StudyBuddyTheme
import com.example.studybuddy.ui.theme.gradient1
import com.example.studybuddy.ui.theme.gradient2
import com.example.studybuddy.ui.theme.gradient3
import com.example.studybuddy.ui.theme.gradient4
import com.example.studybuddy.ui.theme.gradient5
import com.example.studybuddy.view.NavGraphs
import com.ramcosta.composedestinations.DestinationsNavHost

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            StudyBuddyTheme {
                DestinationsNavHost(navGraph = NavGraphs.root)
                }
            }
        }
    }


val taskitems = listOf(
    Task(
        title = "Complete the math assignment",
        description = "Complete the math assignment",
        dueDate = System.currentTimeMillis() + 86400000L, // 1 day from now
        priority = 3,
        relatedToSubject = "Math",
        isCompleted = false,
        taskSubjectId = 1,
        TaskId = 1
    ),
    Task(
        title = "Read chapter 4 of science textbook",
        description = "Read chapter 4 of science textbook",
        dueDate = System.currentTimeMillis() + 2 * 86400000L, // 2 days from now
        priority = 2,
        relatedToSubject = "Science",
        isCompleted = false,
        taskSubjectId = 5,
        TaskId = 1
    ),
    Task(
        title = "Write history essay",
        description = "Write history essay",
        dueDate = System.currentTimeMillis() + 3 * 86400000L, // 3 days from now
        priority = 1,
        relatedToSubject = "History",
        isCompleted = true,
        taskSubjectId = 4,
        TaskId = 1
    ),
    Task(
        title = "Prepare for English literature exam",
        description = "Prepare for English literature exam",
        dueDate = System.currentTimeMillis() + 4 * 86400000L, // 4 days from now
        priority = 2,
        relatedToSubject = "English",
        isCompleted = false,
        taskSubjectId = 3,
        TaskId = 1
    ),
    Task(
        title = "Finish the art project",
        description = "Finish the art project",
        dueDate = System.currentTimeMillis() + 5 * 86400000L, // 5 days from now
        priority = 1,
        relatedToSubject = "Art",
        isCompleted = true,
        taskSubjectId = 2,
        TaskId = 1
    )
)

val sessions = listOf(
    Session(
        sessionSubjectId = 1,
        sessionId = 1,
        date = System.currentTimeMillis() + 86400000L, // 1 day from now
        duration = 3600000L, // 1 hour
        relatedToSubject = "Math"
    ),
    Session(
        sessionSubjectId = 2,
        sessionId = 2,
        date = System.currentTimeMillis() + 2 * 86400000L, // 2 days from now
        duration = 7200000L, // 2 hours
        relatedToSubject = "Science"
    ),
    Session(
        sessionSubjectId = 3,
        sessionId = 3,
        date = System.currentTimeMillis() + 3 * 86400000L, // 3 days from now
        duration = 5400000L, // 1.5 hours
        relatedToSubject = "History"
    ),
    Session(
        sessionSubjectId = 4,
        sessionId = 4,
        date = System.currentTimeMillis() + 4 * 86400000L, // 4 days from now
        duration = 1800000L, // 30 minutes
        relatedToSubject = "English"
    ),
    Session(
        sessionSubjectId = 5,
        sessionId = 5,
        date = System.currentTimeMillis() + 5 * 86400000L, // 5 days from now
        duration = 14400000L, // 4 hours
        relatedToSubject = "Art"
    )
)


val subjects = listOf(
    Subject(
        name = "Math",
        goalHour = 10f,
        color = gradient5, // Use appropriate color objects or gradients
        SubjectId = 1
    ),
    Subject(
        name = "Science",
        goalHour = 8f,
        color = gradient4, // Use appropriate color objects or gradients
        SubjectId = 2
    ),
    Subject(
        name = "History",
        goalHour = 6f,
        color = gradient3, // Use appropriate color objects or gradients
        SubjectId = 3
    ),
    Subject(
        name = "English",
        goalHour = 7f,
        color = gradient2, // Use appropriate color objects or gradients
        SubjectId = 4
    ),
    Subject(
        name = "Art",
        goalHour = 5f,
        color = gradient1, // Use appropriate color objects or gradients
        SubjectId = 5
    )
)

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    StudyBuddyTheme {
    }
}