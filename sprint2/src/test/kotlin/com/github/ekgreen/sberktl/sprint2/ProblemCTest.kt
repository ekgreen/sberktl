import com.github.ekgreen.sberktl.sprint2.ProblemC
import org.junit.jupiter.api.Test

class ProblemCTest : BaseTest(workDir = "sprint2/problemC/2") {

    @Test
    fun testSolution() {
        ProblemC().main()
        checkOutput()
    }
}