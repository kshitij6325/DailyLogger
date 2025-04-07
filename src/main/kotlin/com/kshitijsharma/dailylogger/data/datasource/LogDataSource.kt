package com.kshitijsharma.dailylogger.data.datasource

import com.kshitijsharma.dailylogger.data.model.DailyLog
import java.time.LocalDate

interface LogDataSource {

    suspend fun addLog(log: DailyLog): Result<Unit>

    suspend fun getLogs(): Result<List<DailyLog>>

    suspend fun getLogsBetween(from: Long, to: Long): Result<List<DailyLog>>
}