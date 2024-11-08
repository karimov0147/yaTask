package com.example.yatask.ui.screens.noteInfoScreen

import com.example.yatask.data.models.TodoItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

interface NoteInfoViewModel {

    val todoItem : StateFlow<TodoItem?>

    fun getNoteById(id : String)

    fun saveNote(note: TodoItem)

}