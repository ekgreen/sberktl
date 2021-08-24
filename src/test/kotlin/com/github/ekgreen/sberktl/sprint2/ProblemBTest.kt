import com.github.ekgreen.sberktl.sprint2.ProblemB
import org.junit.jupiter.api.Test

class ProblemBTest : BaseTest(workDir = "sprint2/problemB") {

    @Test
    fun testSolution() {
        ProblemB().main()
        checkOutput()
    }
}