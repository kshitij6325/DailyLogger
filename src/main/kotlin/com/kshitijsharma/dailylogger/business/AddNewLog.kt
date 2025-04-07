package com.kshitijsharma.dailylogger.business

import com.kshitijsharma.dailylogger.data.datasource.LogDataSource
import com.kshitijsharma.dailylogger.data.model.DailyLog
import java.time.LocalDate

class AddNewLog(private val logDataSource: LogDataSource) {

    suspend fun invoke(log: String): Result<Unit> {
        return logDataSource.addLog(DailyLog(System.currentTimeMillis(), log, LocalDate.now().toString()))
    }
}