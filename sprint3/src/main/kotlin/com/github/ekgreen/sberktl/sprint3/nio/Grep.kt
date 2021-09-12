package ru.sber.nio

import java.io.BufferedWriter
import java.io.FileWriter
import java.nio.file.Files
import java.nio.file.Paths
import kotlin.io.path.isRegularFile
import kotlin.io.path.name
import kotlin.io.path.useLines

/**
 * Реализовать простой аналог утилиты grep с использованием калссов из пакета java.nio.
 */
object Grep {
    // Поиск внутри каталога 'logs'
    const val DIR = "logs"

    // Название результирующего файла
    const val RESULT_FILE_NAME = "result.txt"

    /**
     * Метод должен выполнить поиск подстроки subString во всех файлах каталога logs.
     * Каталог logs размещен в данном проекте (io/logs) и внутри содержит другие каталоги.
     * Результатом работы метода должен быть файл в каталоге io(на том же уровне, что и каталог logs), с названием result.txt.
     * Формат содержимого файла result.txt следующий:
     * имя файла, в котором найдена подстрока : номер строки в файле : содержимое найденной строки
     * Результирующий файл должен содержать данные о найденной подстроке во всех файлах.
     * Пример для подстроки "22/Jan/2001:14:27:46":
     * 22-01-2001-1.log : 3 : 192.168.1.1 - - [22/Jan/2001:14:27:46 +0000] "POST /files HTTP/1.1" 200 - "-"
     */
    fun find(subString: String) {
        val regex: Regex = Regex.fromLiteral(subString)
        val logs = Paths.get(DIR)
        var lineNumber: Int = 0

        BufferedWriter(FileWriter(RESULT_FILE_NAME, false)).use { stream ->
            Files.walk(logs)
                .filter { it.isRegularFile() }
                .forEach { file ->
                    file.useLines {
                        for (line in it) {
                            lineNumber += 1
                            if (line.contains(regex))
                                stream.write("${file.name} : $lineNumber : $line\n")
                        }
                        lineNumber = 0
                    }
                }
        }
    }
}