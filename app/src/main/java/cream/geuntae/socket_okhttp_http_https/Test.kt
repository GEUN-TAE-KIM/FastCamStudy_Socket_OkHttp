package cream.geuntae.socket_okhttp_http_https

import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.ServerSocket

fun main() {

    // 메인 쓰레드에서 동작이 안되기 때문에 새로운 쓰레드를 만들어서 동작을 시켜야 함
    // 네트워크 프레임은 소켓으로 하고 OkHttp는 이것을 쉽게 구현하게 해주는 것
    Thread {

        val port = 8080
        val server = ServerSocket(port)

        // 서버는 무한 루프를 돌면서 클라이언트의 요청을 받음
        while (true) {

            val socket = server.accept()

            // 클라이언트로부터 들어오는 스트림 == OutputStream
            // socket.getInputStream()

            // 클라이언트에게 데이터를 주는 스트림 == InputStream
            // socket.getOutputStream()

            //BufferedReader 클래스는 클라이언트로부터 들어오는 데이터를 읽어오고
            val reader = BufferedReader(InputStreamReader(socket.getInputStream()))

            // PrintWriter 클래스는 클라이언트에게 데이터를 전송
            val printer = PrintWriter(socket.getOutputStream())

            var input: String? = "-1"
            while (input != null && input != "") {
                input = reader.readLine()
            }

            println("READ DATA $input")

            // 순서가 틀리면 안됌

            printer.println("HTTP/1.1 200 OK")
            printer.println("Content-Type: text/html\r\n")

            printer.println("<h1>Hello World</h1>")
            printer.println("\r\n")

            // 배출 -> 데이터 전송이 완료되면, flush() 메서드를 사용하여 출력 버퍼를 비우고
            printer.flush()
            printer.close()

            reader.close()

            socket.close()
        }

    }.start()


}