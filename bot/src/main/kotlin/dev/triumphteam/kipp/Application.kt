package dev.triumphteam.kipp

import dev.triumphteam.kipp.func.tokenFromFlag

fun main(args: Array<String>)  {
    Kipp(tokenFromFlag(args))
}

/*fun JdaApplication.module() {
    install(Config)
    install(Database)
    install(Scheduler)

    listen(JdaApplication::messageListener)
    listen(JdaApplication::pasteListener)
    listen(JdaApplication::guildListener)
    listen(JdaApplication::memberListener)
    listen(JdaApplication::tempCommandListener)

    // Temporary
    jda.upsertCommand("about", "Information about Kipp").queue()

    repeatingTask(period = hours(24), delay = MINUTES_TILL_MIDNIGHT, task = ::checkOldMessages)
}*/
