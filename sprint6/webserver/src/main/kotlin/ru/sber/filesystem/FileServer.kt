package ru.sber.filesystem

import java.io.*
import java.lang.StringBuilder
import java.net.ServerSocket
import java.net.Socket
import java.net.URI

/**
 * A basic and very limited implementation of a file server that responds to GET
 * requests from HTTP clients.
 */
class FileServer {

    /**
     * Main entrypoint for the basic file server.
     *
     * @param socket Provided socket to accept connections on.
     * @param fs     A proxy filesystem to serve files from. See the VFilesystem
     *               class for more detailed documentation of its usage.
     * @throws IOException If an I/O error is detected on the server. This
     *                     should be a fatal error, your file server
     *                     implementation is not expected to ever throw
     *                     IOExceptions during normal operation.
     */
    @Throws(IOException::class)
    fun run(socket: ServerSocket, fs: VFilesystem) {

        /**
         * Enter a spin loop for handling client requests to the provided
         * ServerSocket object.
         */
        while (true) {

            // Use socket.accept to get a Socket object
            val accept: Socket = socket.accept()

            /*
            * Using Socket.getInputStream(), parse the received HTTP
            * packet. In particular, we are interested in confirming this
            * message is a GET and parsing out the path to the file we are
            * GETing. Recall that for GET HTTP packets, the first line of the
            * received packet will look something like:
            *
            *     GET /path/to/file HTTP/1.1
            */
            val request: HttpRequest = readHttpMessage(accept.getInputStream())

            /*
             * Using the parsed path to the target file, construct an
             * HTTP reply and write it to Socket.getOutputStream(). If the file
             * exists, the HTTP reply should be formatted as follows:
             *
             *   HTTP/1.0 200 OK\r\n
             *   Server: ru.sber.filesystem.FileServer\r\n
             *   \r\n
             *   FILE CONTENTS HERE\r\n
             *
             * If the specified file does not exist, you should return a reply
             * with an error code 404 Not Found. This reply should be formatted
             * as:
             *
             *   HTTP/1.0 404 Not Found\r\n
             *   Server: ru.sber.filesystem.FileServer\r\n
             *   \r\n
             *
             * Don't forget to close the output stream.
             */
            val file: String? = fs.readFile(VPath(request.uri))

            if (isFileExists(file))
                sendResponse(HttpStatus.HTTP_200, accept.getOutputStream(), request, file)
            else
                sendResponse(HttpStatus.HTTP_404, accept.getOutputStream(), request)
        }
    }

    private fun sendResponse(status: HttpStatus, os: OutputStream, request: HttpRequest, file: String? = null) {
        os.bufferedWriter().use { stream ->
            // append headers
            val header: String = "${request.version} ${status.code} ${status.description}"
            val serverHeader: String = "Server: ${javaClass.name}"

            stream.write(header + RESPONSE_DELIMITER)
            stream.write(serverHeader + RESPONSE_DELIMITER + RESPONSE_DELIMITER)

            // append body if exists
            if (file != null && file.isNotEmpty())
                stream.write(file + RESPONSE_DELIMITER)

            stream.flush()
            stream.close()
        }
    }

    private fun isFileExists(file: String?): Boolean {
        return file != null
    }

    private fun readHttpMessage(stream: InputStream): HttpRequest {
        val header = stream.bufferedReader().readLine()

        val (method, uri, protocol) = header.toString().split(' ')
        return HttpRequest(method, uri, protocol)
    }

    companion object {
        const val RESPONSE_DELIMITER: String = "\r\n"
    }
}