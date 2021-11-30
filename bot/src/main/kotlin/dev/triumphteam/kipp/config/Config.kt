package dev.triumphteam.kipp.config

import dev.triumphteam.core.TriumphApplication
import dev.triumphteam.core.configuration.BaseConfig
import dev.triumphteam.core.configuration.ConfigFeature
import dev.triumphteam.core.feature.attribute.AttributeKey
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