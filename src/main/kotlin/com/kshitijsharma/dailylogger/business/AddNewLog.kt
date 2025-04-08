package com.kshitijsharma.dailylogger.business

import com.kshitijsharma.dailylogger.data.datasource.LogDataSource
import com.kshitijsharma.dailylogger.data.model.DailyLog
import java.time.Clock
import java.time.LocalDate
import java.time.ZoneId

class AddNewLog(
    private val logDataSource: LogDataSource,
    private val clock: Clock = Clock.system(ZoneId.systemDefault())
) {

    suspend fun invoke(log: String): Result<Unit> {
        return logDataSource.addLog(DailyLog(System.currentTimeMillis(), log, LocalDate.now(clock).toString()))
    }
}