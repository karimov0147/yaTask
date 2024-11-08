package com.example.yatask.ui.screens.homeScreen

import androidx.compose.runtime.snapshots.SnapshotStateList
import com.example.yatask.data.models.TodoItem
import kotlinx.coroutines.flow.StateFlow

interface HomeViewModel {

    val viewState : StateFlow<Boolean>

    fun changeViewState(boolean: Boolean)

    fun getAllNoteList(isCompleted : Boolean = false) : SnapshotStateList<TodoItem>

    fun doneTask(note : TodoItem)

    fun removeTask(note : TodoItem)
}