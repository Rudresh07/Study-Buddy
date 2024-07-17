package com.example.studybuddy.domain.model.repository

import com.example.studybuddy.domain.model.Session
import kotlinx.coroutines.flow.Flow

interface SessionRepository {

    suspend fun insertSession(session: Session)

    suspend fun deleteSession(session: Session)

    fun getAllSessions(): Flow<List<Session>>

    suspend fun getSessionBySubjectId(subjectId: Int): Flow<List<Session>>

    fun getTotalDurationBySubjectId(subjectId: Int): Flow<Long>

    fun getTotalDurationBySubjectId(): Flow<Long>

    suspend fun deleteSessionBySubjectId(subjectId: Int)
}