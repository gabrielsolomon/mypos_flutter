package ro.gabrielsolomon.mypos_flutter

import android.app.Activity
import android.util.Log
import androidx.annotation.NonNull
import com.mypos.slavesdk.Currency
import com.mypos.slavesdk.POSHandler
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.embedding.engine.plugins.activity.ActivityAware
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result

/** MyposFlutterPlugin */
class MyposFlutterPlugin : FlutterPlugin, MethodCallHandler, ActivityAware {
    private val TAG = "MyposPlugin"

    private val mPOSHandler: POSHandler? = null

    private var operations: MutableMap<String, MyPosPluginResponseWrapper> = mutableMapOf()
    private lateinit var currentOperation: MyPosPluginResponseWrapper

    /// The MethodChannel that will the communication between Flutter and native Android
    ///
    /// This local reference serves to register the plugin with the Flutter Engine and unregister it
    /// when the Flutter Engine is detached from the Activity
    private lateinit var channel: MethodChannel
    private lateinit var activity: Activity

    override fun onAttachedToEngine(@NonNull flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
        Log.d(TAG, "onAttachedToEngine")
        channel = MethodChannel(flutterPluginBinding.binaryMessenger, "mypos_flutter")
        channel.setMethodCallHandler(this)
    }

    override fun onDetachedFromEngine(@NonNull binding: FlutterPlugin.FlutterPluginBinding) {
        Log.d(TAG, "onDetachedFromEngine")
        channel.setMethodCallHandler(null)
    }

    override fun onAttachedToActivity(binding: ActivityPluginBinding) {
        Log.d(TAG, "onAttachedToActivity")
        activity = binding.activity
        POSHandler.setApplicationContext(activity);
    }

    override fun onDetachedFromActivityForConfigChanges() {
        Log.d(TAG, "onDetachedFromActivityForConfigChanges")
    }

    override fun onReattachedToActivityForConfigChanges(binding: ActivityPluginBinding) {
        Log.d(TAG, "onReattachedToActivityForConfigChanges")
    }

    override fun onDetachedFromActivity() {
        Log.d(TAG, "onDetachedFromActivity")
    }

    override fun onMethodCall(@NonNull call: MethodCall, @NonNull result: Result) {
        Log.d(TAG, "onMethodCall: ${call.method}")

        if (!operations.containsKey(call.method)) {
            operations[call.method] = MyPosPluginResponseWrapper(call.method, result)
        }

        currentOperation = operations[call.method]!!
        currentOperation.methodResult = result
        currentOperation.response = MyPosPluginResponse(call.method)

        when (call.method) {
            "getPlatformVersion" -> result.success("Android ${android.os.Build.VERSION.RELEASE}")
            "setCurrency" -> setCurrency(call.arguments as String).flutterResult()
            else -> result.notImplemented()
        }
    }

    private fun setCurrency(@NonNull currency: String): MyPosPluginResponseWrapper {
        val currentOp = operations["setCurrency"]!!

        try {
            when (currency) {
                "HRK" -> POSHandler.setCurrency(Currency.HRK)
                "CZK" -> POSHandler.setCurrency(Currency.CZK)
                "DKK" -> POSHandler.setCurrency(Currency.DKK)
                "HUF" -> POSHandler.setCurrency(Currency.HUF)
                "ISK" -> POSHandler.setCurrency(Currency.ISK)
                "NOK" -> POSHandler.setCurrency(Currency.NOK)
                "SEK" -> POSHandler.setCurrency(Currency.SEK)
                "CHF" -> POSHandler.setCurrency(Currency.CHF)
                "GBP" -> POSHandler.setCurrency(Currency.GBP)
                "USD" -> POSHandler.setCurrency(Currency.USD)
                "RON" -> POSHandler.setCurrency(Currency.RON)
                "BGN" -> POSHandler.setCurrency(Currency.BGN)
                "EUR" -> POSHandler.setCurrency(Currency.EUR)
                "PLN" -> POSHandler.setCurrency(Currency.PLN)

                else -> throw java.lang.Exception("Invalid currency passed [$currency]")
            }
            currentOp.response.status = true
        } catch (e: Exception) {
            currentOp.response.message = mutableMapOf("errors" to e.message)
            currentOp.response.status = false
        }
        return currentOp
    }
}
