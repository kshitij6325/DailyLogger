package com.kshitijsharma.dailylogger.data.datasource

import com.kshitijsharma.dailylogger.data.model.DailyLog

interface SummaryDataSource {
    suspend fun summary(logs: List<DailyLog>): Result<String>
}