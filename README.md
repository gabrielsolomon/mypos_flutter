# mypos_flutter

Bridge with native MyPos SDK

/!\ WORK IN PROGRESS /!\
Currently still testing and developing full implementation of the SDK. 

ONLY WORKS ON ANDROID FOR NOW

Feel free to add your contribution to this project by submitting pull request or opening issues


## Installation
Use this package as a library
1. Depend on it
Add this to your package's pubspec.yaml file:

```` dart
dependencies:
  mypos_flutter: ^0.1.0
````

2. Install it
You can install packages from the command line:

with Flutter:
````
$ flutter packages get
````

Alternatively, your editor might support flutter packages get. Check the docs for your editor to learn more.

## Getting Started

#### Initialising the library

```dart
import 'package:mypos_flutter/mypos_flutter.dart';
import 'package:mypos_flutter/currency.dart';
import 'package:mypos_flutter/language.dart';
import 'package:mypos_flutter/connection_type.dart';

MyposFlutter.setCurrency(Currency.RON);
MyposFlutter.setConnectionType(ConnectionType.BLUETOOTH);
MyposFlutter.setLanguage(Language.ROMANIAN);

MyposFlutter.setConnectionListener((dynamic event) {
  print("CONNECTION LISTENER FLUTTER");
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
});

```

#### Making a payment
import 'package:mypos_flutter/mypos_flutter.dart';
import 'package:mypos_flutter/receipt_configuration.dart';

```dart

MyposFlutter.purchase(
  "12.34" /*amount*/,
  "9999999999" /*transaction reference*/,
  ReceiptConfiguration.PRINT_AUTOMATICALLY,
);
```

## Methods supported
- [x] POSHandler.setCurrency
- [x] POSHandler.setApplicationContext
- [x] POSHandler.clearDefaultReceiptConfig
- [x] POSHandler.setLanguage
- [x] POSHandler.setConnectionType
- [x] POSHandler.connectDevice
- [x] POSHandler.isTerminalBusy
- [x] POSHandler.setConnectionListener
- [x] POSHandler.setPOSInfoListener
- [ ] POSHandler.setTransactionClearedListener
