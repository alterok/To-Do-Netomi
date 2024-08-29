package com.learn.to_do_netomi.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.learn.to_do_netomi.data.local.model.TaskEntity

@Database(entities = [TaskEntity::class], version = 1, exportSchema = true)
abstract class ToDoTaskDatabase : RoomDatabase() {
    abstract fun getTaskDao() : TaskDao
}