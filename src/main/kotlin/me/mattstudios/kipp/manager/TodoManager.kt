package me.mattstudios.kipp.manager

import me.mattstudios.kipp.settings.Config
import me.mattstudios.kipp.settings.Setting

/**
 * @author Matt
 */
class TodoManager(private val config: Config) {

    fun create(todo: String) {
        val todos = mutableListOf<String>()
        todos.addAll(config[Setting.TODOS])

        todos.add(todo)
        config[Setting.TODOS] = todos
    }

    fun getTodos(): List<String> {
        return config[Setting.TODOS]
    }

    fun remove(index: Int): Boolean {
        val todos = mutableListOf<String>()
        todos.addAll(config[Setting.TODOS])

        if (index > todos.size || index < 1) return false

        todos.removeAt(index - 1)
        config[Setting.TODOS] = todos

        return true
    }

}