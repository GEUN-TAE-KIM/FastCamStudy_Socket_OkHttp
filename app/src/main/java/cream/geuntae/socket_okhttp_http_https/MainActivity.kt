package cream.geuntae.socket_okhttp_http_https

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import com.google.gson.Gson
import okhttp3.*
import org.w3c.dom.Text
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.io.PrintWriter
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.ServerSocket
import java.net.Socket
import java.net.URL

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val client = OkHttpClient()
        val editText = findViewById<EditText>(R.id.serverHostEditText)
        val confirmButton = findViewById<Button>(R.id.confirmButton)
        val informationTextView = findViewById<TextView>(R.id.informationTextView)
        var serverHost = ""

        editText.addTextChangedListener {
            serverHost = it.toString()
        }

        confirmButton.setOnClickListener {
            val request: Request = Request.Builder()
                .url("http://$serverHost:8080")
                .build()

            val callback = object : Callback {

                override fun onFailure(call: Call, e: IOException) {

                    runOnUiThread {
                        Toast.makeText(this@MainActivity, "수신에 실패", Toast.LENGTH_SHORT).show()
                    }
                    Log.e("Client", e.toString())
                }

                override fun onResponse(call: Call, response: Response) {

                    if (response.isSuccessful) {

                        //string()은 바디를 스트링으로 불러온다는 거지만
                        //toString()은 바디의 헤시값 구현되어 있는 값을 불러오는 것
                        val response = response.body?.string()

                        val message = Gson().fromJson(response, Message::class.java)

                        runOnUiThread {
                            informationTextView.isVisible = true
                            informationTextView.text = message.message

                            editText.isVisible = false
                            confirmButton.isVisible = false
                        }

                    } else {
                        Toast.makeText(this@MainActivity, "수신에 실패", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            client.newCall(request).enqueue(callback)
        }


        // 메인 쓰레드에서 동작이 안되기 때문에 새로운 쓰레드를 만들어서 동작을 시켜야 함
        // 네트워크 프레임은 소켓으로 하고 OkHttp는 이것을 쉽게 구현하게 해주는 것
        /*Thread {
            try {
                val socket = Socket("10.0.2.2", 8080)
                val printer = PrintWriter(socket.getOutputStream())
                val reader = BufferedReader(InputStreamReader(socket.getInputStream()))

                printer.println("GET / HTTP/1.1")
                printer.println("Host: 127.0.0.1:8080")
                printer.println("User-Agent: android")
                printer.println("\r\n")
                printer.flush()

                var input: String? = "-1"
                while (input != null) {
                    input = reader.readLine()
                    Log.e("Client", "$input")
                }

                reader.close()
                printer.close()
                socket.close()
            } catch (e: Exception) {
                Log.e("Client", e.toString())
            }

        }.start()*/

    }
}









