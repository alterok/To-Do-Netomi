package com.learn.to_do_netomi.presentation.misc

enum class TaskSort(val id: Int) {
    DEFAULT(0),
    ASCENDING(1),
    DESCENDING(2);

    companion object {
        fun fromId(id: Int) = entries.find { it.id == id }
    }
}