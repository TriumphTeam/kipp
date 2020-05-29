package me.mattstudios.kipp.utils

import me.mattstudios.kipp.data.Cache
import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.entities.Member
import java.awt.Color
import java.io.BufferedReader
import java.io.DataOutputStream
import java.io.IOException
import java.io.InputStreamReader
import java.net.URL
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets
import java.util.concurrent.CompletableFuture
import java.util.regex.Pattern
import javax.net.ssl.HttpsURLConnection


object Utils {

    // Pattern to identify URLs in the message
    private val urlPattern = Pattern.compile(
            "(?:^|[\\W])((ht|f)tp(s?):\\/\\/|www\\.)"
            + "(([\\w\\-]+\\.){1,}?([\\w\\-.~]+\\/?)*"
            + "[\\p{Alnum}.,%_=?&#\\-+()\\[\\]\\*$~@!:/{};']*)",
            Pattern.CASE_INSENSITIVE or Pattern.MULTILINE or Pattern.DOTALL)

    /**
     * Makes the word plural if the count is not one
     */
    fun String.plural(count: Int) = if (count != 1) this.plus("s") else this

    /**
     * Reads the content of an input stream and turns it into a string
     */
    fun URL.readContent(charset: Charset = Charsets.UTF_8) = openStream().bufferedReader(charset).use { it.readText() }
    
    /**
     * Gets the color from HEX code
     */
    fun hexToRgb(colorStr: String): Color {
        return Color(
                Integer.valueOf(colorStr.substring(1, 3), 16),
                Integer.valueOf(colorStr.substring(3, 5), 16),
                Integer.valueOf(colorStr.substring(5, 7), 16))
    }

    /**
     * Sets all the default roles to the player
     */
    fun setRoles(cache: Cache, guild: Guild, member: Member) {
        val memberRole = cache.memberRole
        if (memberRole != null && !member.roles.contains(memberRole)) {
            guild.addRoleToMember(member, memberRole).queue()
        }
    }

    /**
     * Creates a help chat paste
     */
    fun createPaste(text: String): String {
        val future = CompletableFuture.supplyAsync {
            val postData = text.toByteArray(StandardCharsets.UTF_8)
            val postDataLength = postData.size

            val requestURL = "https://paste.helpch.at/documents"

            val connection = URL(requestURL).openConnection() as HttpsURLConnection

            connection.doOutput = true
            connection.instanceFollowRedirects = false
            connection.requestMethod = "POST"
            connection.setRequestProperty("User-Agent", "Kipp")
            connection.setRequestProperty("Content-Length", postDataLength.toString())
            connection.useCaches = false

            var response = ""
            val dataOutputStream: DataOutputStream
            try {
                dataOutputStream = DataOutputStream(connection.outputStream)
                dataOutputStream.write(postData)
                val reader = BufferedReader(InputStreamReader(connection.inputStream))
                response = reader.readLine()
            } catch (e: IOException) {
                e.printStackTrace()
            }


            if (!response.contains("\"key\"")) return@supplyAsync ""


            return@supplyAsync response.substring(response.indexOf(":") + 2, response.length - 2)
        }

        return "https://paste.helpch.at/".plus(future.get())
    }

    /**
     * Gets all the links in a message
     */
    fun String.extractLinks(): List<URL> {
        val links = mutableListOf<URL>()

        val matcher = urlPattern.matcher(this)
        while (matcher.find()) {
            val matchStart = matcher.start(1)
            val matchEnd = matcher.end()

            links.add(URL(this.substring(matchStart, matchEnd)))
        }

        return links
    }

}