package com.kshitijsharma.dailylogger.data.datasource

import com.kshitijsharma.dailylogger.data.model.DailyLog
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File
import java.time.LocalDate

class JSONFileDataSource(private val path: String) : LogDataSource {

    private val logFile by lazy {
        File(path).also {
            if (!it.exists()) {
                it.createNewFile()
                it.writeText("[]")
            }
        }
    }

    override suspend fun addLog(log: DailyLog): Result<Unit> {
        return runCatching {
            val logs = logFile.parse().toMutableList()
            val dateStr = LocalDate.now().toString()
            val existingIndex = logs.indexOfFirst { it.date == dateStr }

            if (existingIndex != -1) {
                val updatedLog = logs[existingIndex].copy(
                    log = logs[existingIndex].log + "\n" + log.log
                )
                logs[existingIndex] = updatedLog
            } else {
                logs.add(log)
            }

            logFile.writeText(Json.encodeToString(logs))
        }
    }

    override suspend fun getLogs(): Result<List<DailyLog>> {
        return Result.success(logFile.parse())

    }

    override suspend fun getLogsBetween(from: Long, to: Long): Result<List<DailyLog>> {
        return runCatching {
            logFile.parse().filter { it.timestamp in from..to }
        }
    }
}

private fun File.parse() = Json.decodeFromString<List<DailyLog>>(readText())