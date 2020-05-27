package me.mattstudios.kipp.utils

import me.mattstudios.kipp.data.Cache
import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.entities.Member
import java.awt.Color
import java.io.BufferedReader
import java.io.DataOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.net.URL
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets
import java.util.concurrent.CompletableFuture
import javax.net.ssl.HttpsURLConnection


object Utils {

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
     * Makes the word plural if the count is not one
     */
    fun String.plural(count: Int) = if (count != 1) this.plus("s") else this

    /**
     * Reads the content of an input stream and turns it into a string
     */
    fun InputStream.readContent(charset: Charset = Charsets.UTF_8) = bufferedReader(charset).use { it.readText() }

}