package cream.geuntae.socket_okhttp_http_https

import com.google.gson.annotations.SerializedName

data class Message(
    @SerializedName("message")
    val message: String,
)
