import com.github.ekgreen.sberktl.sprint2.ProblemD
import org.junit.jupiter.api.Test

class ProblemDTest : BaseTest(workDir = "sprint2/problemD") {

    @Test
    fun testSolution() {
        ProblemD().main()
        checkOutput()
    }
}