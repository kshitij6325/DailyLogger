package com.kshitijsharma.dailylogger.data.datasource

import com.kshitijsharma.dailylogger.data.model.DailyLog
import kotlin.math.log

class NormalSummaryDataSource : SummaryDataSource {
    override suspend fun summary(logs: List<DailyLog>): Result<String> {
        val summary = StringBuilder()
        logs.forEach {
            summary.append(it.log)
            summary.append("\n")
        }
        return Result.success(summary.toString())
    }
}