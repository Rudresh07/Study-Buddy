package com.example.studybuddy.view.subject

import androidx.lifecycle.ViewModel
import com.example.studybuddy.domain.model.repository.SubjectRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SubjectViewModel @Inject constructor(
    private val subjectRepository: SubjectRepository
): ViewModel() {





}