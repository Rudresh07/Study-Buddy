package com.example.studybuddy.view

import androidx.lifecycle.ViewModel
import com.example.studybuddy.domain.model.repository.SubjectRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val subjectRepository: SubjectRepository
):ViewModel()
{
}