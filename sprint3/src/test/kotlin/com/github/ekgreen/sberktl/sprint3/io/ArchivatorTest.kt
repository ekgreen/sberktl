package com.github.ekgreen.sberktl.sprint3.io

import org.junit.jupiter.api.Test
import ru.sber.io.Archivator

internal class ArchivatorTest{

    @Test
    fun `zipLogfile() check method`(){
        Archivator.zipLogfile()
    }

    @Test
    fun `unzipLogfile() check method`(){
        Archivator.unzipLogfile()
    }
}