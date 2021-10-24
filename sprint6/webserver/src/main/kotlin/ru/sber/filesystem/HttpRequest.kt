package ru.sber.filesystem

import java.net.URI

data class HttpRequest(val method: String, val uri: String, val version: String)
