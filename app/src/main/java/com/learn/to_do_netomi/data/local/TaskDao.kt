package com.learn.to_do_netomi.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import com.learn.to_do_netomi.data.local.model.TaskEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {
    @Upsert
    fun insertTask(task: TaskEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTasks(tasks: List<TaskEntity>)

    @Delete
    fun removeTask(task: TaskEntity)

    @Query("Select * from tasks")
    fun getAllTasksFlow() : Flow<List<TaskEntity>>
}