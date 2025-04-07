package com.kshitijsharma.dailylogger

import java.lang.Exception


private const val USAGE = "Usage: dailylogger log <your log>"

fun main(args: Array<String>) {
    check(args).onFailure {
        println(it.message)
    }.onSuccess {
        println(it)
    }
}

fun check(arg: Array<String>): Result<String> {
    return if (arg.isEmpty() || arg[0] != COMMAND.LOG.command) {
        Result.failure(Exception(USAGE))
    } else if (arg[1].isEmpty()) {
        Result.failure(Exception("Cannot log empty activity"))
    } else {
        DailyLogger.registerLogs(arg[1])
    }
}

sealed class COMMAND(val command: String) {
    object LOG : COMMAND("log")
}