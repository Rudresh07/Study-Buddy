package com.example.studybuddy.view.session

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.example.studybuddy.utils.ServiceConstants.Action_Service_Cancel
import com.example.studybuddy.utils.ServiceConstants.Action_Service_Start
import com.example.studybuddy.utils.ServiceConstants.Action_Service_Stop

class StudySessionTimerService : Service() {
    override fun onBind(intent: Intent?): IBinder? = null
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.action.let {
            when (it) {
                Action_Service_Start -> {

                }


                Action_Service_Stop -> {

                }

                Action_Service_Cancel -> {
                }
            }
            return super.onStartCommand(intent, flags, startId)
        }
    }
}