package com.example.yatask.data.repository.impl

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.example.yatask.data.models.TodoItem
import com.example.yatask.data.repository.TodoItemsRepository
import com.example.yatask.utils.Importance
import java.util.Date


class TodoItemRepositoryImpl : TodoItemsRepository {
    private val taskList = mutableStateListOf(
        TodoItem("1" , "Купить что-то" , Importance.NORMAL , Date() , true , Date() , Date() ),
        TodoItem("2" , "Купить что-то, где-то, зачем-то, но зачем не очень понятно" , Importance.HIGH , Date() , false , Date() , Date() ) ,
        TodoItem("3" , "Купить что-то, где-то, зачем-то, но зачем не очень понятно, но точно чтобы показать как обр…" , Importance.LOW , Date() , false , Date() , Date() ) ,
        TodoItem("4" , "Купить что-то" , Importance.HIGH , Date() , false , Date() , Date() ),
        TodoItem("5" , "Купить что-то, где-то, зачем-то, но зачем не очень понятно" , Importance.NORMAL , Date() , true , Date() , Date() ) ,
        TodoItem("6" , "Купить что-то, где-то, зачем-то, но зачем не очень понятно, но точно чтобы показать как обр…" , Importance.LOW , Date() , false , Date() , Date() ) ,
        )

    companion object{
        private var todoItemsRepository : TodoItemsRepository? = null

        fun initRepository(){
            todoItemsRepository = TodoItemRepositoryImpl()
        }
        fun getInstance() = todoItemsRepository!!
    }

    override var notionStateList: SnapshotStateList<TodoItem> = taskList

    override fun getAllNoteList(isCompleted: Boolean): SnapshotStateList<TodoItem> {
        return if (isCompleted) {
            val newList = mutableStateListOf<TodoItem>()
            notionStateList.forEach {
                if (!it.isCompleted) newList.add(it)
            }
            newList
        }else {
            notionStateList
        }
    }

    override fun addNote(note: TodoItem) {
        taskList.add(note)
    }

    override fun removeNote(note: TodoItem) {
        notionStateList.remove(note)
    }

    override fun editNote(note: TodoItem) {
        for (it in taskList.indices){
            if (taskList[it].id == note.id){
                taskList[it] = note
            }
        }
    }

    override fun findNote(id: String): TodoItem? {
        for (it in taskList.indices){
            if (taskList[it].id == id){
               return taskList[it]
            }else{
                continue
            }
        }
        return null
    }


}