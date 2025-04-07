package com.kshitijsharma.dailylogger

import java.time.LocalDate

object DailyLogger {

    fun registerLogs(log: String): Result<String> {
        val date = LocalDate.now()
        return Result.success("Log for $date: $log")
    }
}