package com.example.studybuddy.view.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.studybuddy.R
import com.example.studybuddy.domain.model.Session

fun LazyListScope.StudySessionList(
    sectionTitle: String,
    Sessions: List<Session>,
    emptyListText: String,
    onDeleteItemClick: (Session) -> Unit
) {
    item {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = sectionTitle,
                    modifier = Modifier.padding(12.dp),
                    style = MaterialTheme.typography.bodySmall
                )

            }
        }
    }
    if (Sessions.isEmpty()) {
        item {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Image(
                    modifier = Modifier
                        .size(120.dp),
                    painter = painterResource(id = R.drawable.lamp),
                    contentDescription = emptyListText
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = emptyListText,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
    items(Sessions) { session ->
        SessionCard(
            session = session,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
            onDeleteItemClick = { onDeleteItemClick(session) }
        )
    }
}

@Composable
private fun SessionCard(
    modifier: Modifier = Modifier,
    session: Session,
    onDeleteItemClick: () -> Unit = {}
) {
    val formattedDuration = formatDuration(session.duration)

    Card(modifier = modifier) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = session.relatedToSubject,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = "${session.date}",
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = formattedDuration,
                    style = MaterialTheme.typography.bodySmall
                )
            }
            IconButton(onClick = { onDeleteItemClick() }) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete Session"
                )
            }
        }
    }
}

// Function to format duration in hours and minutes
fun formatDuration(durationMillis: Long): String {
    val hours = durationMillis / (1000 * 60 * 60)
    val minutes = (durationMillis % (1000 * 60 * 60)) / (1000 * 60)
    return "${hours}h ${minutes}m"
}
