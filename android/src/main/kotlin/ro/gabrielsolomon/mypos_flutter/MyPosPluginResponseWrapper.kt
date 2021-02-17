package ro.gabrielsolomon.mypos_flutter

import io.flutter.plugin.common.MethodChannel.Result
import androidx.annotation.NonNull

class MyPosPluginResponseWrapper (@NonNull var methodName: String, @NonNull var methodResult: Result) {
    lateinit var response: MyPosPluginResponse
    fun flutterResult() {
        methodResult.success(response.toMap())
    }
}

class MyPosPluginResponse(@NonNull var methodName: String) {
    var status: Boolean = false
    lateinit var message: MutableMap<String, Any?>
    fun toMap(): Map<String, Any?> {
        return mapOf("status" to status, "message" to message, "methodName" to methodName)
    }
}