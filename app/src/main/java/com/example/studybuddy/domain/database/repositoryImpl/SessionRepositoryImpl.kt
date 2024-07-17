package com.example.studybuddy.domain.database.repositoryImpl

import com.example.studybuddy.domain.database.local.SessionDao
import com.example.studybuddy.domain.model.Session
import com.example.studybuddy.domain.model.repository.SessionRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SessionRepositoryImpl @Inject constructor(
    private val sessionDao: SessionDao
):SessionRepository {
    override suspend fun insertSession(session: Session) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteSession(session: Session) {
        TODO("Not yet implemented")
    }

    override fun getAllSessions(): Flow<List<Session>> {
        TODO("Not yet implemented")
    }

    override suspend fun getSessionBySubjectId(subjectId: Int): Flow<List<Session>> {
        TODO("Not yet implemented")
    }

    override fun getTotalDurationBySubjectId(subjectId: Int): Flow<Long> {
        TODO("Not yet implemented")
    }

    override fun getTotalDurationBySubjectId(): Flow<Long> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteSessionBySubjectId(subjectId: Int) {
        TODO("Not yet implemented")
    }
}