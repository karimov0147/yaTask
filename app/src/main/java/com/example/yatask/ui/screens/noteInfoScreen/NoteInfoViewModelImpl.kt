package com.example.yatask.ui.screens.noteInfoScreen

import androidx.lifecycle.ViewModel
import com.example.yatask.data.models.TodoItem
import com.example.yatask.data.repository.TodoItemsRepository
import com.example.yatask.data.repository.impl.TodoItemRepositoryImpl
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class NoteInfoViewModelImpl : NoteInfoViewModel , ViewModel() {
    private val todoItemsRepository : TodoItemsRepository = TodoItemRepositoryImpl.getInstance()
    private val _todoItem : MutableStateFlow<TodoItem?> = MutableStateFlow(null)
    override val todoItem : StateFlow<TodoItem?> = _todoItem.asStateFlow()

    override fun getNoteById(id: String){
        _todoItem.value = todoItemsRepository.findNote(id)
    }

    override fun saveNote(note: TodoItem) {
        todoItemsRepository.addNote(note)
    }


}