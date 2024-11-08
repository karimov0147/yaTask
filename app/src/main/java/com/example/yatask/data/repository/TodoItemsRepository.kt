package com.example.yatask.data.repository

import androidx.compose.runtime.snapshots.SnapshotStateList
import com.example.yatask.data.models.TodoItem

interface TodoItemsRepository {

    val notionStateList : SnapshotStateList<TodoItem>

    fun getAllNoteList(isCompleted : Boolean) : SnapshotStateList<TodoItem>

    fun addNote(note : TodoItem)

    fun removeNote(note: TodoItem)

    fun editNote(note: TodoItem)

    fun findNote(id : String) : TodoItem?

}