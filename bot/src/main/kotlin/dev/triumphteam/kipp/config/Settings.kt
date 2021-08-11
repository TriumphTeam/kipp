package dev.triumphteam.kipp.config

import me.mattstudios.config.SettingsHolder
import me.mattstudios.config.properties.Property

object Settings : SettingsHolder {

    //val DATABASE = Property.create()
    val LOG_MESSAGES = Property.create(listOf())

}