package com.kshitijsharma.dailylogger

import DisplayAllLogsTillDate
import com.kshitijsharma.dailylogger.business.AddNewLog
import com.kshitijsharma.dailylogger.data.datasource.JSONFileDataSource
import kotlinx.coroutines.*
import java.lang.Exception


private const val HELP = "dailylogger - A simple CLI tool to record and view your daily logs.\n" +
        "\n" +
        "Usage:\n" +
        "  dailylogger log <text to log>      Add a log entry for the current day.\n" +
        "  dailylogger show                   Display all logs till date (scrollable).\n" +
        "  dailylogger summary                AI-generated summary of all logs till date.\n" +
        "  dailylogger --help                 Show this help message.\n" +
        "  dailylogger --version              Show the current version.\n" +
        "\n" +
        "Examples:\n" +
        "  dailylogger log \"Worked on the new onboarding flow\"\n" +
        "  dailylogger show\n" +
        "  dailylogger summary\n" +
        "\n" +
        "Logs are stored at: ~/dailylogger.json\n"

private const val ERROR = "Invalid options for dailylogger. Check usage with --help"

private val logsPath = System.getProperty("user.home") + "/dailylogger.json"

private val scope by lazy { CoroutineScope(Dispatchers.Default + SupervisorJob()) }
private val fileLogDataSource by lazy { JSONFileDataSource(logsPath) }
private val addNewLog by lazy { AddNewLog(fileLogDataSource) }
private val displayAllLogsTillDate by lazy { DisplayAllLogsTillDate(fileLogDataSource) }

fun main(args: Array<String>) = runBlocking {
    val job = scope.launch {
        check(args).onFailure {
            println(it.message)
        }.onSuccess {
            println(it)
        }
    }
    job.join()
}

suspend fun check(arg: Array<String>): Result<Any> {
    return when {
        arg.isEmpty() -> Result.failure(Exception(ERROR))

        arg[0] == COMMAND.LOG.command -> {
            if (arg.size < 2 || arg[1].isBlank()) {
                Result.failure(Exception("Cannot log empty activity"))
            } else {
                addNewLog.invoke(arg[1])
            }
        }

        arg[0] == COMMAND.HELP.command -> Result.success(HELP)

        arg[0] == COMMAND.VERSION.command -> Result.success(version())

        arg[0] == COMMAND.SHOW.command -> displayAllLogsTillDate.invoke()

        else -> Result.failure(Exception(ERROR))
    }
}

private fun version(): String {
    return object {}.javaClass.getResource("/VERSION")
        ?.readText()
        ?.trim() ?: "Unknown"
}

sealed class COMMAND(val command: String) {
    data object LOG : COMMAND("log")
    data object SHOW : COMMAND("show")
    data object HELP : COMMAND("--help")
    data object VERSION : COMMAND("--version")
    data object SUMMARY : COMMAND("summary")
}