import 'package:flutter/material.dart';
import 'package:mypos_flutter_example/init.dart';
import 'package:mypos_flutter_example/ready.dart';

import 'app_state.dart';
import 'in_transaction.dart';

void main() {
  runApp(MyApp());
}

class MyApp extends StatefulWidget {
  @override
  _MyAppState createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  ApplicationState _state = ApplicationState.Inactive;

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Plugin example app'),
        ),
        body: getBodyWidget(),
      ),
    );
  }

  updateState(newState) {
    setState(() {
      _state = newState;
    });
  }

  Widget getBodyWidget() {
    switch (_state) {
      case ApplicationState.Inactive:
        return InitScreen(this.updateState);
      case ApplicationState.Ready:
        return ReadyScreen();
      case ApplicationState.InTransaction:
        return InTransactionScreen();
    }

    return InitScreen(this.updateState);
  }
}
