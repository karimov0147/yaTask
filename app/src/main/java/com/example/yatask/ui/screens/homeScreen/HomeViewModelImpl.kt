package com.example.yatask.ui.screens.homeScreen

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import com.example.yatask.data.models.TodoItem
import com.example.yatask.data.repository.TodoItemsRepository
import com.example.yatask.data.repository.impl.TodoItemRepositoryImpl
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class HomeViewModelImpl : HomeViewModel , ViewModel() {
    private val todoItemsRepository : TodoItemsRepository = TodoItemRepositoryImpl.getInstance()
    private val _viewState  = MutableStateFlow<Boolean>(false)
    override val viewState: StateFlow<Boolean> = _viewState.asStateFlow()

    override fun changeViewState(boolean: Boolean) {
        _viewState.value = boolean
    }


    override fun getAllNoteList(isCompleted: Boolean): SnapshotStateList<TodoItem> {
        return todoItemsRepository.getAllNoteList(isCompleted)
    }

    override fun doneTask(note : TodoItem){
        todoItemsRepository.editNote(note.copy(isCompleted = true))
    }

    override fun removeTask(note : TodoItem){
        todoItemsRepository.removeNote(note)
    }


}