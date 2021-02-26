import 'package:flutter/material.dart';
import 'package:mypos_flutter/mypos_flutter.dart';
import 'package:mypos_flutter/receipt_configuration.dart';

class ReadyScreen extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return Column(mainAxisAlignment: MainAxisAlignment.start, children: [
      Container(
        height: 15,
      ),
      Center(
        child: Text('READY'),
      ),
      Container(
        height: 15,
      ),
      Center(
        child: MaterialButton(
          onPressed: () {
            MyposFlutter.purchase(
              "12.34",
              "9999999999" /*transaction reference*/,
              ReceiptConfiguration.PRINT_AUTOMATICALLY,
            );
          },
          child: Container(
            child: Column(
              children: [
                Text("12.34 lei"),
              ],
            ),
          ),
          padding: new EdgeInsets.all(15.0),
          color: Colors.blue,
        ),
      ),
    ]);
  }
}
