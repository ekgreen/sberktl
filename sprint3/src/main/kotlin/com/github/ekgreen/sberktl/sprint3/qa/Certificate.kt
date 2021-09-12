package ru.sber.qa

/**
 * Готовая справка.
 */
class Certificate(
    val certificateRequest: CertificateRequest,
    val processedBy: Long,
    val data: ByteArray,
) {

}
