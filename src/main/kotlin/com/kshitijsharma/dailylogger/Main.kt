package com.kshitijsharma.dailylogger

import DisplayAllLogsTillDate
import com.kshitijsharma.dailylogger.business.AddNewLog
import com.kshitijsharma.dailylogger.data.datasource.JSONFileDataSource
import kotlinx.coroutines.*
import java.lang.Exception


private const val USAGE = "Usage: dailylogger log <your log> | show"
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
        arg.isEmpty() -> Result.failure(Exception(USAGE))

        arg[0] == COMMAND.LOG.command -> {
            if (arg.size < 2 || arg[1].isBlank()) {
                Result.failure(Exception("Cannot log empty activity"))
            } else {
                addNewLog.invoke(arg[1])
            }
        }

        arg[0] == COMMAND.SHOW.command -> displayAllLogsTillDate.invoke()

        else -> Result.failure(Exception(USAGE))
    }
}

sealed class COMMAND(val command: String) {
    data object LOG : COMMAND("log")
    data object SHOW : COMMAND("show")
}