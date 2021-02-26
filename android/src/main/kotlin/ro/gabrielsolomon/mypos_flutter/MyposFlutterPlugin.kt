package ro.gabrielsolomon.mypos_flutter

import android.app.Activity
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.annotation.NonNull
import com.mypos.slavesdk.ConnectionType
import com.mypos.slavesdk.Currency
import com.mypos.slavesdk.Language
import com.mypos.slavesdk.POSHandler
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.embedding.engine.plugins.activity.ActivityAware
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding
import io.flutter.plugin.common.EventChannel
import io.flutter.plugin.common.EventChannel.EventSink
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result


/** MyposFlutterPlugin */
class MyposFlutterPlugin : FlutterPlugin, MethodCallHandler, ActivityAware {
    private val TAG = "MyposPlugin"

    private val mPOSHandler: POSHandler? = null

    private val uiThreadHandler: Handler = Handler(Looper.getMainLooper())

    private var operations: MutableMap<String, MyPosPluginResponseWrapper> = mutableMapOf()
    private var currentOperation: MyPosPluginResponseWrapper? = null

    /// The MethodChannel that will the communication between Flutter and native Android
    ///
    /// This local reference serves to register the plugin with the Flutter Engine and unregister it
    /// when the Flutter Engine is detached from the Activity
    private lateinit var channel: MethodChannel

    private var _eventSinkConnection: EventSink? = null
    private var _eventSinkPOSReady: EventSink? = null

    private lateinit var activity: Activity

    override fun onAttachedToEngine(@NonNull flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
        Log.d(TAG, "onAttachedToEngine")

        POSHandler.setApplicationContext(flutterPluginBinding.getApplicationContext());

        channel = MethodChannel(flutterPluginBinding.binaryMessenger, "mypos_flutter")
        channel.setMethodCallHandler(this)

        val eventChannelConnection = EventChannel(flutterPluginBinding.binaryMessenger, "mypos_flutter.events.connection")
        eventChannelConnection.setStreamHandler(object : EventChannel.StreamHandler {
            override fun onListen(arguments: Any?, eventSink: EventSink?) {
                _eventSinkConnection = eventSink;
            }

            override fun onCancel(arguments: Any?) {
                TODO("Not yet implemented")
            }
        })

        val eventChannelPOSReady = EventChannel(flutterPluginBinding.binaryMessenger, "mypos_flutter.events.pos_ready")
        eventChannelPOSReady.setStreamHandler(object : EventChannel.StreamHandler {
            override fun onListen(arguments: Any?, eventSink: EventSink?) {
                _eventSinkPOSReady = eventSink;
            }

            override fun onCancel(arguments: Any?) {
                TODO("Not yet implemented")
            }
        })
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
        currentOperation!!.methodResult = result
        currentOperation!!.response = MyPosPluginResponse(call.method)
        currentOperation!!.response.message = mutableMapOf()

        when (call.method) {
            "getPlatformVersion" -> result.success("Android ${android.os.Build.VERSION.RELEASE}")
            "setCurrency" -> setCurrency(call.arguments as String).flutterResult()
            "setLanguage" -> setLanguage(call.arguments as String).flutterResult()
            "setConnectionType" -> setConnectionType(call.arguments as String).flutterResult()
            "clearDefaultReceiptConfig" -> clearDefaultReceiptConfig().flutterResult()
            "setConnectionListener" -> setConnectionListener().flutterResult()
            "setPOSReadyListener" -> setPOSReadyListener().flutterResult()
            "connectDevice" -> connectDevice().flutterResult()
            "purchase" -> purchase(
                    call.argument<String?>("amount"),
                    call.argument<String?>("tranRef"),
                    call.argument<String?>("receiptConfiguration"))
                    .flutterResult()
            else -> result.notImplemented()
        }
    }

    private fun clearDefaultReceiptConfig(): MyPosPluginResponseWrapper {
        val currentOp = operations["clearDefaultReceiptConfig"]!!
        POSHandler.clearDefaultReceiptConfig();

        currentOp.response.status = true
        return currentOp
    }

    private fun setCurrency(@NonNull currency: String): MyPosPluginResponseWrapper {
        val currentOp = operations["setCurrency"]!!

        try {
            when (currency) {
                "Currency.HRK" -> POSHandler.setCurrency(Currency.HRK)
                "Currency.CZK" -> POSHandler.setCurrency(Currency.CZK)
                "Currency.DKK" -> POSHandler.setCurrency(Currency.DKK)
                "Currency.HUF" -> POSHandler.setCurrency(Currency.HUF)
                "Currency.ISK" -> POSHandler.setCurrency(Currency.ISK)
                "Currency.NOK" -> POSHandler.setCurrency(Currency.NOK)
                "Currency.SEK" -> POSHandler.setCurrency(Currency.SEK)
                "Currency.CHF" -> POSHandler.setCurrency(Currency.CHF)
                "Currency.GBP" -> POSHandler.setCurrency(Currency.GBP)
                "Currency.USD" -> POSHandler.setCurrency(Currency.USD)
                "Currency.RON" -> POSHandler.setCurrency(Currency.RON)
                "Currency.BGN" -> POSHandler.setCurrency(Currency.BGN)
                "Currency.EUR" -> POSHandler.setCurrency(Currency.EUR)
                "Currency.PLN" -> POSHandler.setCurrency(Currency.PLN)

                else -> throw java.lang.Exception("Invalid currency passed [$currency]")
            }

            currentOp.response.message = mutableMapOf("currency" to currency)
            currentOp.response.status = true
        } catch (e: Exception) {
            currentOp.response.message = mutableMapOf("errors" to e.message)
            currentOp.response.status = false
        }
        return currentOp
    }

    private fun setLanguage(@NonNull language: String): MyPosPluginResponseWrapper {
        val currentOp = operations["setLanguage"]!!

        try {
            when (language) {
                "ENGLISH" -> POSHandler.setLanguage(Language.ENGLISH)
                "GERMAN" -> POSHandler.setLanguage(Language.GERMAN)
                "ITALIAN" -> POSHandler.setLanguage(Language.ITALIAN)
                "SPANISH" -> POSHandler.setLanguage(Language.SPANISH)
                "FRENCH" -> POSHandler.setLanguage(Language.FRENCH)
                "CROATIAN" -> POSHandler.setLanguage(Language.CROATIAN)
                "ROMANIAN" -> POSHandler.setLanguage(Language.ROMANIAN)
                "CZECH" -> POSHandler.setLanguage(Language.CZECH)
                "PORTUGUESE" -> POSHandler.setLanguage(Language.PORTUGUESE)
                "BULGARIAN" -> POSHandler.setLanguage(Language.BULGARIAN)
                "SWEDISH" -> POSHandler.setLanguage(Language.SWEDISH)
                "GREEK" -> POSHandler.setLanguage(Language.GREEK)
                "DUTCH" -> POSHandler.setLanguage(Language.DUTCH)
                "LATVIAN" -> POSHandler.setLanguage(Language.LATVIAN)
                "ICELANDIC" -> POSHandler.setLanguage(Language.ICELANDIC)
                "SLOVENIAN" -> POSHandler.setLanguage(Language.SLOVENIAN)
                "LITHUANIAN" -> POSHandler.setLanguage(Language.LITHUANIAN)
                "POLISH" -> POSHandler.setLanguage(Language.POLISH)
                "HUNGARIAN" -> POSHandler.setLanguage(Language.HUNGARIAN)

                else -> throw java.lang.Exception("Invalid language passed [$language]")
            }
            currentOp.response.status = true
        } catch (e: Exception) {
            currentOp.response.message = mutableMapOf("errors" to e.message)
            currentOp.response.status = false
        }
        return currentOp
    }

    private fun setConnectionType(@NonNull connectionType: String): MyPosPluginResponseWrapper {
        val currentOp = operations["setConnectionType"]!!

        try {
            when (connectionType) {
                "ConnectionType.BLUETOOTH" -> {
                    POSHandler.setConnectionType(ConnectionType.BLUETOOTH)
                    AppPermissions().isLocationPermissionGranted(this.activity)
                }
                "ConnectionType.USB" -> POSHandler.setConnectionType(ConnectionType.USB)

                else -> throw java.lang.Exception("Invalid language passed [$connectionType]")
            }

            currentOp.response.message = mutableMapOf("ConnectionType" to connectionType)
            currentOp.response.status = true
        } catch (e: Exception) {
            currentOp.response.message = mutableMapOf("errors" to e.message)
            currentOp.response.status = false
        }
        return currentOp
    }

    private fun setConnectionListener(): MyPosPluginResponseWrapper {
        val currentOp = operations["setConnectionListener"]!!

        try {
            POSHandler.getInstance().setConnectionListener {
                val map: MutableMap<String, Any?> = HashMap()
                map["event"] = "onConnected";
                uiThreadHandler.post { this._eventSinkConnection?.success(map) }
            }
            currentOp.response.status = true
        } catch (e: Exception) {
            currentOp.response.message = mutableMapOf("errors" to e.message)
            currentOp.response.status = false
        }
        return currentOp
    }

    private fun setPOSReadyListener(): MyPosPluginResponseWrapper {
        val currentOp = operations["setPOSReadyListener"]!!

        try {
            POSHandler.getInstance().setPOSReadyListener {

                val map: MutableMap<String, Any?> = HashMap()
                map["event"] = "onPOSReady";
                uiThreadHandler.post { this._eventSinkPOSReady?.success(map) }
            }
            currentOp.response.status = true
        } catch (e: Exception) {
            currentOp.response.message = mutableMapOf("errors" to e.message)
            currentOp.response.status = false
        }
        return currentOp
    }

    private fun connectDevice(): MyPosPluginResponseWrapper {
        val currentOp = operations["connectDevice"]!!

        println("Method connectDevice")

        try {
            POSHandler.getInstance().connectDevice(activity)
            currentOp.response.status = true
        } catch (e: Exception) {
            currentOp.response.message = mutableMapOf("errors" to e.message)
            currentOp.response.status = false
        }
        return currentOp
    }

    private fun purchase(
            amount: String?,
            tranRef: String?,
            receiptConfiguration: String?
    ): MyPosPluginResponseWrapper {
        val currentOp = operations["purchase"]!!

        try {

            val receiptConfigurationInt : Int = when (receiptConfiguration) {
                "ReceiptConfiguration.PRINT_AUTOMATICALLY" -> 0
                "ReceiptConfiguration.PRINT_AFTER_CONFIRMATION" -> 1
                "ReceiptConfiguration.PRINT_ONLY_MERCHANT_COPY" -> 2
                "ReceiptConfiguration.DO_NOT_PRINT" -> 3
                "ReceiptConfiguration.E_RECEIPT" -> 4

                else -> 0
            }

            POSHandler.getInstance().purchase(
                amount /*amount*/,
                tranRef /*transaction reference*/,
                receiptConfigurationInt /*receipt configuration*/
            );

            currentOp.response.message = mutableMapOf()
            currentOp.response.status = true
        } catch (e: Exception) {
            currentOp.response.message = mutableMapOf("errors" to e.message)
            currentOp.response.status = false
        }
        return currentOp
    }
}
