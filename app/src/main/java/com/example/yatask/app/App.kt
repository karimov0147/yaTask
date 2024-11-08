package com.example.yatask.app

import android.app.Application
import com.example.yatask.data.repository.impl.TodoItemRepositoryImpl

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        TodoItemRepositoryImpl.initRepository()
    }
}