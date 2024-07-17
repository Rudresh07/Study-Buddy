package com.example.studybuddy.domain.database.repositoryImpl

import com.example.studybuddy.domain.database.local.TaskDao
import com.example.studybuddy.domain.model.Task
import com.example.studybuddy.domain.model.repository.TaskRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TaskRepositoryImpl @Inject constructor(
    private val taskDao: TaskDao
):TaskRepository {
    override suspend fun upsertTask(task: Task) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteTaskById(taskId: Int) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteTaskBySubjectId(subjectId: Int) {
        TODO("Not yet implemented")
    }

    override suspend fun getTaskByTaskId(taskId: Int): Task? {
        TODO("Not yet implemented")
    }

    override fun getTaskBySubjectId(subjectId: Int): Flow<List<Task>> {
        TODO("Not yet implemented")
    }

    override fun getAllTasks(): Flow<List<Task>> {
        TODO("Not yet implemented")
    }
}