package me.mattstudios.kipp.manager

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import me.mattstudios.kipp.data.Database

/**
 * @author Matt
 */
class TodoManager(private val database: Database) {

    private val todos = mutableMapOf<String, String>()

    init {
        todos.putAll(database.getTodos())
    }

    fun create(id: String, todo: String) {
        GlobalScope.launch { database.insertTodo(id, todo) }
        todos[id] = todo
    }

    fun getTodos(): Map<String, String> {
        return todos
    }

    fun remove(id: String) {
        database.deleteTodo(id)
        todos.remove(id)
    }

}