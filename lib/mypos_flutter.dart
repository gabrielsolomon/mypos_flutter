import 'dart:async';

import 'package:flutter/services.dart';
import 'package:mypos_flutter/MyPosPluginResponse.dart';
import 'package:mypos_flutter/currency.dart';

class MyposFlutter {
  static const MethodChannel _channel = const MethodChannel('mypos_flutter');

  static Future<String> get platformVersion async {
    final String version = await _channel.invokeMethod('getPlatformVersion');
    return version;
  }

  static Future<MyPosPluginResponse> setCurrency(Currency currency) async {
    final response = MyPosPluginResponse.fromMap(
        await _channel.invokeMethod('setCurrency', currency.toString()));
    return response;
  }
}
