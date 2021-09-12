package ru.sber.io

import java.io.*
import java.nio.ByteBuffer
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream
import java.util.zip.ZipOutputStream

/**
 * Реализовать методы архивации и разархивации файла.
 * Для реализиации использовать ZipInputStream и ZipOutputStream.
 */
object Archivator {

    const val FILE_NAME: String = "logfile.log"
    const val ZIP_NAME: String = "logfile.zip"
    const val UNZIPPED_FILE_NAME: String = "unzippedLogfile.log"

    const val BUFFER_SIZE: Int = 100 // например, 100 :|

    /**
     * Метод, который архивирует файл logfile.log в архив logfile.zip.
     * Архив должен располагаться в том же каталоге, что и исходный файл.
     */
    fun zipLogfile() {
        val buffer: ByteArray = ByteArray(BUFFER_SIZE)

        BufferedInputStream(File(FILE_NAME).inputStream()).use { inputStream ->
            ZipOutputStream(BufferedOutputStream(FileOutputStream(ZIP_NAME))).use {
                // 1. создадим 'сущность' - logfile.log
                it.putNextEntry(ZipEntry(FILE_NAME))

                // 2. будем читать из файла и записывать в буфер и 'сущность'
                var read: Int = inputStream.read(buffer)
                while (read != -1) {
                    it.write(buffer, 0, read)
                    read = inputStream.read(buffer)
                }

                // 3. Завершим запись сущности и сбросим буфер фс
                it.closeEntry()
                it.flush()
                // В Java, я бы написал buffer = null, а вот в котлине не очень понятно что мне сделать
                // Можно было бы переменную сделать var и ByteArray?, но весь код начинает гореть как
                // елка на новый год ( как будто котлин немного против )
            }
        }
    }

    /**
     * Метод, который извлекает файл из архива.
     * Извлечь из архива logfile.zip файл и сохарнить его в том же каталоге с именем unzippedLogfile.log
     */
    fun unzipLogfile() {
        val buffer: ByteArray = ByteArray(BUFFER_SIZE)

        ZipInputStream(BufferedInputStream(FileInputStream(ZIP_NAME))).use { inputStream ->
            BufferedOutputStream(FileOutputStream(UNZIPPED_FILE_NAME)).use {
                inputStream.nextEntry!!

                var read = inputStream.read(buffer)
                while (read != -1) {
                    it.write(buffer, 0, read)
                    read = inputStream.read(buffer)
                }

                inputStream.closeEntry()
                it.flush()
            }
        }
    }
}