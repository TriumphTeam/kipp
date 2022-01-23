package me.mattstudios.kipp.commands

import com.google.api.client.auth.oauth2.Credential
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.jackson2.JacksonFactory
import com.google.api.client.util.store.FileDataStoreFactory
import com.google.api.services.sheets.v4.Sheets
import com.google.api.services.sheets.v4.SheetsScopes
import me.mattstudios.kipp.utils.MessageUtils.queueMessage
import me.mattstudios.mfjda.annotations.Command
import me.mattstudios.mfjda.annotations.Default
import me.mattstudios.mfjda.annotations.Prefix
import me.mattstudios.mfjda.base.CommandBase
import java.io.File
import java.io.FileNotFoundException
import java.io.IOException
import java.io.InputStreamReader


/**
 * @author Matt
 */
@Prefix("!")
@Command("add")
class Test : CommandBase() {

    @Default
    fun testAdd() {
        val HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport()
        val spreadsheetId = "1BgIP4SPMbufMPuSqpiYeqA_GvrBsXE5Djpg6ZU_CLeQ"
        val range = "A:C"
        val service = Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                .setApplicationName(APPLICATION_NAME)
                .build()
        val response = service.spreadsheets().values()[spreadsheetId, range]
                .execute()
        val values: List<List<Any>> = response.getValues()

        if (values.isEmpty()) {
            println("No data found.")
        } else {
            // Get the index of the column that needs to be filtered
            val filterIndex = values.first().indexOf("Name")

            // Filters the name that starts with J
            val filteredValues = values
                    .map { it[filterIndex] }
                    .filter { (it as String).startsWith("j", true) }

            message.textChannel.queueMessage(filteredValues.joinToString(", "))
        }
    }


    @Throws(IOException::class)
    private fun getCredentials(HTTP_TRANSPORT: NetHttpTransport): Credential? {
        // Load client secrets.
        val `in` = Test::class.java.getResourceAsStream(CREDENTIALS_FILE_PATH)
                ?: throw FileNotFoundException("Resource not found: $CREDENTIALS_FILE_PATH")
        val clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, InputStreamReader(`in`))

        // Build flow and trigger user authorization request.
        val flow = GoogleAuthorizationCodeFlow.Builder(HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(FileDataStoreFactory(File(TOKENS_DIRECTORY_PATH)))
                .setAccessType("offline")
                .build()

        val receiver = LocalServerReceiver.Builder().setPort(8888).build()

        return AuthorizationCodeInstalledApp(flow, receiver).authorize("user")
    }

    companion object {
        private val APPLICATION_NAME = "Google Sheets API Java Quickstart"
        private val JSON_FACTORY = JacksonFactory.getDefaultInstance()
        private val TOKENS_DIRECTORY_PATH = "tokens"

        /**
         * Global instance of the scopes required by this quickstart.
         * If modifying these scopes, delete your previously saved tokens/ folder.
         */
        private val SCOPES = listOf(SheetsScopes.SPREADSHEETS_READONLY)
        private val CREDENTIALS_FILE_PATH = "/credentials.json"
    }
}