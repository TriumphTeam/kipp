package dev.triumphteam.kipp.listener

import dev.triumphteam.bukkit.feature.feature
import dev.triumphteam.jda.JdaApplication
import dev.triumphteam.kipp.config.Config
import dev.triumphteam.kipp.config.KippColor
import dev.triumphteam.kipp.event.on
import dev.triumphteam.kipp.func.append
import dev.triumphteam.kipp.func.embed
import dev.triumphteam.kipp.func.plural
import dev.triumphteam.kipp.func.queueReply
import dev.triumphteam.kipp.scanner.PasteScanner
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent
import java.io.BufferedReader
import java.io.DataOutputStream
import java.io.InputStreamReader
import java.net.URL
import java.nio.charset.StandardCharsets
import java.util.regex.Pattern
import javax.net.ssl.HttpsURLConnection

// New scope for this
private val scope = CoroutineScope(Dispatchers.IO)

// TODO add more services
private val pasteServices = listOf("pastebin.com", "hastebin.com", "paste.helpch.at")

// Pattern to identify URLs in the message
private val urlPattern = Pattern.compile(
    "(?:^|[\\W])((ht|f)tp(s?):\\/\\/|www\\.)"
            + "(([\\w\\-]+\\.){1,}?([\\w\\-.~]+/?)*"
            + "[\\p{Alnum}.,%_=?&#\\-+()\\[\\]*$~@!:/{};']*)",
    Pattern.CASE_INSENSITIVE or Pattern.MULTILINE or Pattern.DOTALL
)

fun JdaApplication.pasteListener() {
    val config = feature(Config)
    val leakScanner = PasteScanner(config)

    on<GuildMessageReceivedEvent> {
        if (author.isBot) return@on

        scope.launch {
            val links = message.extractLinks()

            if (links.isEmpty()) return@launch

            val newPastes = mutableListOf<String>()
            var leakDetected = false

            for (link in links) {
                if (pasteServices.none { it in link.host }) continue

                val pasteUrl = link.append("/raw")

                val pasteContent = pasteUrl.readText()

                if (!leakDetected) leakDetected = leakScanner.searchForLeaks(pasteContent, message, link)
                else continue

                if (link.host.isPasteBin()) {
                    newPastes.add(createPaste(pasteContent))
                }

                // TODO other detections here
            }

            if (newPastes.isEmpty()) return@launch

            val embed = embed {
                color(KippColor.SUCCESS)
                field("Paste".plural(newPastes.size) + " converted!", newPastes.joinToString("\n"))
                footer("Please use a better paste tool!")
            }

            message.queueReply(embed)
        }
    }
}

/**
 * Checks if the paste is pastebin
 */
private fun String.isPasteBin() = "pastebin" in this

private fun createPaste(text: String): String {
    // Turns the post data into byte array
    val postData = text.toByteArray(StandardCharsets.UTF_8)

    // The request URL
    val requestURL = "https://paste.helpch.at/documents"

    // Opens the connection
    val connection = (URL(requestURL).openConnection() as HttpsURLConnection).apply {
        doOutput = true
        instanceFollowRedirects = false
        requestMethod = "POST"
        setRequestProperty("User-Agent", "Kipp")
        setRequestProperty("Content-Length", postData.size.toString())
        useCaches = false
    }

    // Writes the paste
    DataOutputStream(connection.outputStream).write(postData)

    // Reads the response
    val reader = BufferedReader(InputStreamReader(connection.inputStream))
    val response = reader.readLine()

    val content = if (!response.contains("\"key\"")) ""
    else response.substring(response.indexOf(":") + 2, response.length - 2)

    return "https://paste.helpch.at/".plus(content)
}

/**
 * Gets all the links in a message
 */
private fun Message.extractLinks(): List<URL> {
    val links = mutableListOf<URL>()

    val matcher = urlPattern.matcher(contentRaw)
    while (matcher.find()) {
        val matchStart = matcher.start(1)
        val matchEnd = matcher.end()

        links.add(URL(contentRaw.substring(matchStart, matchEnd)))
    }

    return links
}