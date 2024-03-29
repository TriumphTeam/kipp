package dev.triumphteam.kipp.func

import org.apache.commons.cli.DefaultParser
import org.apache.commons.cli.Option
import org.apache.commons.cli.Options
import java.awt.Color
import java.net.URL

fun tokenFromFlag(args: Array<String>): String {
    val cli = DefaultParser().parse(
        Options().apply {
            addOption(Option.builder("t").hasArg().argName("token").required().build())
        },
        args
    )

    return cli.getOptionValue("t")
}

fun String.toColor(): Color {
    return Color(
        Integer.valueOf(substring(1, 3), 16),
        Integer.valueOf(substring(3, 5), 16),
        Integer.valueOf(substring(5, 7), 16)
    )
}

fun URL.append(string: String) = if (this.path == null || "/raw" in this.path) this else URL(this, string + this.path)

/**
 * Makes the word plural if the count is not one
 */
fun String.plural(count: Int) = if (count != 1) plus("s") else this