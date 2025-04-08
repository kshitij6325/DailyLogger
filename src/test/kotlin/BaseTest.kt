import com.kshitijsharma.dailylogger.data.datasource.JSONFileDataSource
import java.io.File
import kotlin.test.AfterTest

abstract class BaseTest {

    open val fileDataSource by lazy { JSONFileDataSource("./dailylogger.json") }

    @AfterTest
    open fun cleanup() {
        File("./dailylogger.json").delete()
    }
}