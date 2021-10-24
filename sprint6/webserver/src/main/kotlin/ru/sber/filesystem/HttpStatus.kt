package ru.sber.filesystem

enum class HttpStatus(val code: Int,  val description: String) {
    HTTP_200(200, "OK"),
    HTTP_404(404, "Not Found")
}