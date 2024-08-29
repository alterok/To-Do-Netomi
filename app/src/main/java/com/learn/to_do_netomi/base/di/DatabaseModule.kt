package com.learn.to_do_netomi.base.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.learn.to_do_netomi.data.local.ToDoTaskDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun providesTaskDatabase(@ApplicationContext context: Context): ToDoTaskDatabase =
        Room.databaseBuilder(context.applicationContext,
            ToDoTaskDatabase::class.java,
            "todo-db")
            .build()
}