package dev.triumphteam.kipp.config

import dev.triumphteam.bukkit.TriumphApplication
import dev.triumphteam.bukkit.configuration.BaseConfig
import dev.triumphteam.bukkit.configuration.ConfigFeature
import dev.triumphteam.bukkit.feature.attribute.AttributeKey
import me.mattstudios.config.SettingsHolder
import java.nio.file.Path

class Config(path: Path, holder: Class<out SettingsHolder>) : BaseConfig(path, holder) {

    companion object Feature : ConfigFeature<TriumphApplication, Config, Config> {
        override val key: AttributeKey<Config> = AttributeKey("Config")

        override fun install(application: TriumphApplication, configure: Config.() -> Unit): Config {
            return Config(Path.of(application.applicationFolder.path, "config.yml"), Settings::class.java)
        }
    }
}