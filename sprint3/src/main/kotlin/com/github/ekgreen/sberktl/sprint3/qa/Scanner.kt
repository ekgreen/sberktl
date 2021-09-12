package ru.sber.qa

import kotlin.random.Random

object Scanner {
    const val SCAN_TIMEOUT_THRESHOLD = 10_000L

    fun getScanData(): ByteArray {
        val scanDuration = Random.nextLong(5000L, 15000L)
        if (scanDuration > SCAN_TIMEOUT_THRESHOLD) {
            sleep(SCAN_TIMEOUT_THRESHOLD)
            throw ScanTimeoutException()
        } else {
            sleep(scanDuration)
        }
        return Random.nextBytes(100)
    }

    // не придумал как замокать только Thread.sleep
    // PS можно не мокать
    fun sleep(delay: Long){
        Thread.sleep(delay)
    }
}
