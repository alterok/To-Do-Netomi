package com.learn.to_do_netomi.base.di

import android.content.Context
import com.learn.to_do_netomi.data.local.ToDoTaskDatabase
import com.learn.to_do_netomi.data.repository.TaskListPreferenceRepositoryImpl
import com.learn.to_do_netomi.data.repository.TaskRepositoryImpl
import com.learn.to_do_netomi.domain.repository.FakeTaskRepositoryImpl
import com.learn.to_do_netomi.domain.repository.TaskListPreferenceRepository
import com.learn.to_do_netomi.domain.repository.TaskRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun providesTaskRepository(database: ToDoTaskDatabase): TaskRepository =
        TaskRepositoryImpl(database)

    @Provides
    @Singleton
    fun providesTaskListPrefRepository(@ApplicationContext context: Context): TaskListPreferenceRepository =
        TaskListPreferenceRepositoryImpl(context)

}