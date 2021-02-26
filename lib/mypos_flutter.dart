import 'dart:async';

import 'package:flutter/services.dart';
import 'package:mypos_flutter/MyPosPluginResponse.dart';
import 'package:mypos_flutter/connection_type.dart';
import 'package:mypos_flutter/currency.dart';
import 'package:mypos_flutter/language.dart';
import 'package:mypos_flutter/receipt_configuration.dart';

class MyposFlutter {
  static const MethodChannel _channel = const MethodChannel('mypos_flutter');

  static const EventChannel _eventChannelConnection =
      EventChannel('mypos_flutter.events.connection');

  static const EventChannel _eventChannelPOSReady =
      EventChannel('mypos_flutter.events.pos_ready');

  StreamController<dynamic> _eventStreamController =
      StreamController.broadcast();

  static Future<String> get platformVersion async {
    final String version = await _channel.invokeMethod('getPlatformVersion');
    return version;
  }

  static Future<MyPosPluginResponse> setCurrency(Currency currency) async {
    final response = MyPosPluginResponse.fromMap(
        await _channel.invokeMethod('setCurrency', currency.toString()));
    return response;
  }

  static Future<MyPosPluginResponse> setLanguage(Language lang) async {
    final response = MyPosPluginResponse.fromMap(
        await _channel.invokeMethod('setLanguage', lang.toString()));
    return response;
  }

  static Future<MyPosPluginResponse> setConnectionType(
      ConnectionType type) async {
    final response = MyPosPluginResponse.fromMap(
        await _channel.invokeMethod('setConnectionType', type.toString()));
    return response;
  }

  static Future<MyPosPluginResponse> clearDefaultReceiptConfig() async {
    final response = MyPosPluginResponse.fromMap(
        await _channel.invokeMethod('clearDefaultReceiptConfig'));
    return response;
  }

  static Future<MyPosPluginResponse> connectDevice() async {
    final response = MyPosPluginResponse.fromMap(
        await _channel.invokeMethod('connectDevice'));
    return response;
  }

  static Future<MyPosPluginResponse> setPOSReadyListener(_listener) async {
    final response = MyPosPluginResponse.fromMap(
        await _channel.invokeMethod('setPOSReadyListener'));
    _eventChannelPOSReady.receiveBroadcastStream().listen(_listener);
    return response;
  }

  static Future<MyPosPluginResponse> setConnectionListener(_listener) async {
    final response = MyPosPluginResponse.fromMap(
        await _channel.invokeMethod('setConnectionListener'));
    _eventChannelConnection.receiveBroadcastStream().listen(_listener);
    return response;
  }

  static Future<MyPosPluginResponse> purchase(
      String amount /*amount*/,
      String tranRef /*transaction reference*/,
      ReceiptConfiguration receiptConfiguration
      /*receipt configuration*/) async {
    final response = MyPosPluginResponse.fromMap(
        await _channel.invokeMethod('purchase', <String, String>{
      "amount": amount,
      "tranRef": tranRef,
      "receiptConfiguration": receiptConfiguration.toString()
    }));
    return response;
  }
}
