import 'package:flutter/material.dart';
import 'package:mypos_flutter/connection_type.dart';
import 'package:mypos_flutter/currency.dart';
import 'package:mypos_flutter/language.dart';
import 'package:mypos_flutter/mypos_flutter.dart';
import 'package:mypos_flutter/pos_info_states.dart';

import 'app_state.dart';

class InitScreen extends StatelessWidget {
  final void Function(ApplicationState) _stateCallback;

  InitScreen(this._stateCallback);

  @override
  Widget build(BuildContext context) {
    this.initPlatformState();
    return Column(
      children: [
        Center(
          child: Text('Running on: '),
        ),
        RaisedButton(
            child: Text("Connect"),
            onPressed: () {
              MyposFlutter.connectDevice();
            })
      ],
    );
  }

  // Platform messages are asynchronous, so we initialize in an async method.
  Future<void> initPlatformState() async {
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
      _stateCallback(ApplicationState.Ready);
    });

    MyposFlutter.setPOSInfoListener((dynamic event) {
      print("POS INFO FLUTTER");
      print(event);
      var state = PosInfoState.fromEvent(event);
      print("-----------------------");
      print(state);
      print(state.status_code);
      print(state.message);
      print("-----------------------");
      // _stateCallback(ApplicationState.Ready);
    });
  }
}
