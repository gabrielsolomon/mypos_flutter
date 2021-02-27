// Status documentation here: https://github.com/developermypos/myPOS-SDK-Android#pos-info-statuses

class PosInfoStates {
  static const int POS_STATUS_SUCCESS = 0;
  static const int POS_STATUS_PENDING_USER_INTERACTION = 1;
  static const int POS_STATUS_USER_CANCEL = 2;
  static const int POS_STATUS_INTERNAL_ERROR = 3;
  static const int POS_STATUS_TERMINAL_BUSY = 4;
  static const int POS_STATUS_UNSUPPORTED_SDK_VERSION = 5;
  static const int POS_STATUS_NO_UPDATE_FOUND = 6;
  static const int POS_STATUS_MANDATORY_UPDATE = 7;
  static const int POS_STATUS_OPTIONAL_UPDATE = 8;
  static const int POS_STATUS_POS_UPDATING = 9;
  static const int POS_STATUS_ACTIVATION_REQUIRED = 10;
  static const int POS_STATUS_PROCESSING = 11;
  static const int POS_STATUS_DEACTIVATION_NOT_COMPLETED = 12;
  static const int POS_STATUS_ACTIVATION_NOT_REQUIRED = 13;
  static const int POS_STATUS_ACTIVATION_NOT_COMPLETED = 14;
  static const int POS_STATUS_WRONG_ACTIVATION_CODE = 15;
  static const int POS_STATUS_WRONG_DEACTIVATION_CODE = 16;
  static const int POS_STATUS_WAIT_ACTIVATION_CODE = 17;
  static const int POS_STATUS_WAIT_DEACTIVATION_CODE = 18;
  static const int POS_STATUS_UPDATE_NOT_COMPLETED = 19;
  static const int POS_STATUS_TRANSACTION_NOT_FOUND = 20;
  static const int POS_STATUS_NO_PRINTER_AVAILABLE = 21;
  static const int POS_STATUS_NO_PAPER = 22;
  static const int POS_STATUS_WRONG_AMOUNT = 23;
  static const int POS_STATUS_NO_CARD_FOUND = 24;
  static const int POS_STATUS_NOT_SUPPORTED_CARD = 25;
  static const int POS_STATUS_CARD_CHIP_ERROR = 26;
  static const int POS_STATUS_INVALID_PIN = 27;
  static const int POS_STATUS_MAX_PIN_COUNT_EXCEEDED = 28;
  static const int POS_STATUS_PIN_CHECK_ONLINE = 29;
  static const int POS_STATUS_SUCCESS_ACTIVATION = 31;
  static const int POS_STATUS_SUCCESS_DEACTIVATION = 32;
  static const int POS_STATUS_SUCCESS_UPDATE = 33;
  static const int POS_STATUS_SUCCESS_PURCHASE = 34;
  static const int POS_STATUS_SUCCESS_REFUND = 35;
  static const int POS_STATUS_SUCCESS_REPRINT_RECEIPT = 37;
  static const int POS_STATUS_DOWNLOADING_CERTIFICATES_IN_PROGRESS = 38;
  static const int POS_STATUS_DOWNLOADING_CERTIFICATES_COMPLETED = 39;
  static const int POS_STATUS_INCORRECT_PRINT_DATA = 40;
  static const int POS_STATUS_INCORRECT_LOGO_INDEX = 41;
  static const int POS_STATUS_SUCCESS_PRINT_RECEIPT = 42;
}

class PosInfoState {
  final int status_code;
  final String message;

  PosInfoState(this.status_code, this.message);

  static PosInfoState fromEvent(Map event) {
    var state = new PosInfoState(
      int.parse(event["status"].toString()),
      event["description"].toString(),
    );
    return state;
  }
}
