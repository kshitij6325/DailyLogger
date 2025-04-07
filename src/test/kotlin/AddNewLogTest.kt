import com.kshitijsharma.dailylogger.business.AddNewLog
import com.kshitijsharma.dailylogger.data.datasource.JSONFileDataSource
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import java.io.File
import java.time.LocalDate
import kotlin.test.AfterTest

class AddNewLogTest {
    private val testScope = TestScope()
    private val fileDataSource = JSONFileDataSource("./dailylogger.json")
    private val sut = AddNewLog(fileDataSource)


    @AfterTest
    fun cleanup() {
        File("./dailylogger.json").delete()
    }

    @Test
    fun `given new log from console check if it is added to the file`() = runTest {
        val result = sut.invoke("Test log")
        assert(result.isSuccess)
    }

    @Test
    fun `given new log from console check if the date is correct`() = runTest {
        sut.invoke("Test log")
        val logs = fileDataSource.getLogs()
        assert(logs.getOrNull()?.size == 1)
        println(logs.getOrNull()?.get(0))
        assert(logs.getOrNull()?.get(0)?.log == "Test log")
        assert(logs.getOrNull()?.get(0)?.date == LocalDate.now().toString())
    }

    @Test
    fun `given new log from console check if log from same date already exits it appends`() = runTest {
        sut.invoke("Test log")
        assert(fileDataSource.getLogs().getOrNull()?.size == 1)
        sut.invoke("Test log 2")
        val logs = fileDataSource.getLogs()
        assert(logs.getOrNull()?.size == 1)
        assert(logs.getOrNull()?.get(0)?.log == "Test log\nTest log 2")
        assert(logs.getOrNull()?.get(0)?.date == LocalDate.now().toString())
    }
}