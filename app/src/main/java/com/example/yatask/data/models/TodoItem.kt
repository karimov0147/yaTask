package com.example.yatask.data.models

import com.example.yatask.utils.Importance
import java.util.Date


data class TodoItem (
    val id : String,
    val text : String,
    val importance : Importance = Importance.NORMAL,
    val deadline : Date?,
    val isCompleted : Boolean = false,
    val createdAt : Date,
    val modifiedAt : Date
)