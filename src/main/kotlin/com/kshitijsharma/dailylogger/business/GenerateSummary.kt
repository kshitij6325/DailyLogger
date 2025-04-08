package com.kshitijsharma.dailylogger.business

import com.kshitijsharma.dailylogger.data.datasource.LogDataSource
import com.kshitijsharma.dailylogger.data.datasource.SummaryDataSource
import com.kshitijsharma.dailylogger.flatMap
import java.time.Clock
import java.time.LocalDate
import java.time.ZoneId

class GenerateSummary(
    private val logDataSource: LogDataSource,
    private val summaryDataSource: SummaryDataSource,
    private val clock: Clock = Clock.system(ZoneId.systemDefault())
) {
    suspend fun invoke(months: Int): Result<String> {
        val toDate = LocalDate.now(clock)
        val fromDate = toDate.minusMonths(months.toLong())
        return logDataSource
            .getLogsBetween(fromDate, toDate)
            .flatMap { logs -> summaryDataSource.summary(logs) }
    }

}