import com.kshitijsharma.dailylogger.data.datasource.LogDataSource

class DisplayAllLogsTillDate(private val logDataSource: LogDataSource) {

    suspend fun invoke(): Result<String> {
        return logDataSource.getLogs().mapCatching { logs ->
            logs.joinToString(separator = "\n\n") { log ->
                val date = java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(java.util.Date(log.timestamp))
                "$date - ${log.log}"
            }
        }
    }
}
