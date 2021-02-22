import 'dart:async';

import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:mypos_flutter/connection_type.dart';
import 'package:mypos_flutter/currency.dart';
import 'package:mypos_flutter/language.dart';
import 'package:mypos_flutter/mypos_flutter.dart';

void main() {
  runApp(MyApp());
}

class MyApp extends StatefulWidget {
  @override
  _MyAppState createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  String _platformVersion = 'Unknown';

  @override
  void initState() {
    super.initState();
    initPlatformState();
  }

  // Platform messages are asynchronous, so we initialize in an async method.
  Future<void> initPlatformState() async {
    String platformVersion;
    // Platform messages may fail, so we use a try/catch PlatformException.
    try {
      platformVersion = await MyposFlutter.platformVersion;
    } on PlatformException {
      platformVersion = 'Failed to get platform version.';
    }

    MyposFlutter.setCurrency(Currency.RON);
    MyposFlutter.setConnectionType(ConnectionType.BLUETOOTH);
    MyposFlutter.setLanguage(Language.ROMANIAN);
    MyposFlutter.setConnectionListener((dynamic event) {
      print("CONNECTION LISTINER FLUTTER");
      print(event);
      print("-----------------------");
    });

    MyposFlutter.setPOSReadyListener((dynamic event) {
      print("CONNECTION READY FLUTTER");
      print(event);
      print("-----------------------");
    });

    // If the widget was removed from the tree while the asynchronous platform
    // message was in flight, we want to discard the reply rather than calling
    // setState to update our non-existent appearance.
    if (!mounted) return;

    setState(() {
      _platformVersion = platformVersion;
    });
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Plugin example app'),
        ),
        body: Column(
          children: [
            Center(
              child: Text('Running on: $_platformVersion\n'),
            ),
            RaisedButton(
                child: Text("Connect"),
                onPressed: () {
                  MyposFlutter.connectDevice();
                })
          ],
        ),
      ),
    );
  }
}
