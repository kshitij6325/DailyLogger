import com.kshitijsharma.dailylogger.business.GenerateSummary
import com.kshitijsharma.dailylogger.data.datasource.NormalSummaryDataSource
import com.kshitijsharma.dailylogger.data.model.DailyLog
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import java.time.Clock
import java.time.LocalDate
import java.time.ZoneId

class SummaryTest : BaseTest() {
    private val summaryDataSource = NormalSummaryDataSource()
    private val fixedNow = LocalDate.of(2025, 4, 1).atStartOfDay(ZoneId.systemDefault()).toInstant()
    private val clock = Clock.fixed(fixedNow, ZoneId.systemDefault())
    private val sut = GenerateSummary(fileDataSource, summaryDataSource, clock)

    @Test
    fun `given logs for past 40 days check if summary is only generated for last 30 days`() = runTest {

        // Adding logs for last 40 days
        var baseDate = LocalDate.now(clock).minusDays(40)
        for (i in 1..40) {
            val logDate = baseDate.plusDays(i.toLong())
            fileDataSource.addLog(
                DailyLog(
                    timestamp = System.currentTimeMillis(),
                    date = logDate.toString(),
                    log = "Test log $logDate"
                )
            )
        }

        val summary = sut.invoke(1)

        assert(summary.isSuccess)
        summary.onSuccess {
            baseDate = LocalDate.now(clock).minusDays(30)
            for (i in 1..30) {
                val logDate = baseDate.plusDays(i.toLong())
                assert(it.contains(logDate.toString()))
            }
        }
    }
}