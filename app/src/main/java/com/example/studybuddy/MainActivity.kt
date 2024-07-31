package com.example.studybuddy

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.ActivityCompat
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
import com.example.studybuddy.view.destinations.SessionScreenRouteDestination
import com.example.studybuddy.view.session.StudySessionTimerService
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.navigation.dependency
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private var isBound by mutableStateOf(false)
    private lateinit var timerService: StudySessionTimerService
    private val connection =object :ServiceConnection{
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binder = service as StudySessionTimerService.StudySessionTimerBinder
            timerService = binder.getService()
            isBound = true
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            isBound = false
        }

    }

    override fun onStart() {
        super.onStart()
        Intent(this, StudySessionTimerService::class.java).also { intent ->
            bindService(intent, connection, BIND_AUTO_CREATE)
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            if (isBound) {
                StudyBuddyTheme {
                    DestinationsNavHost(navGraph = NavGraphs.root,
                        dependenciesContainerBuilder = {
                            dependency(SessionScreenRouteDestination) {
                                timerService
                            }
                        }
                    )

                }
            }
        }
        requestPermission()
        }

    private fun requestPermission(){
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.TIRAMISU)
        {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.POST_NOTIFICATIONS),
                0)
        }
    }
    override fun onStop() {
        super.onStop()
            unbindService(connection)
            isBound = false

    }

    }




