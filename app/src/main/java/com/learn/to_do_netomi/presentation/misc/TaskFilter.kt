package com.learn.to_do_netomi.presentation.misc

enum class TaskFilter(val id: Int) {
    ALL(0),
    TODAY(1),
    TOMORROW(2),
    THIS_WEEK(3);

    companion object{
        fun fromId(id:Int) = entries.find { it.id == id }
    }
}