package com.kshitijsharma.dailylogger.data.model

import kotlinx.serialization.Serializable
import java.time.LocalDate

@Serializable
data class DailyLog(val timestamp: Long, val log: String, val date: String)