import com.github.ekgreen.sberktl.sprint2.ProblemA
import org.junit.jupiter.api.Test

class ProblemATest : BaseTest(workDir = "sprint2/problemA") {

    @Test
    fun testSolution() {
        ProblemA().main()
        checkOutput()
    }
}