/// Response returned from native platform
class MyPosPluginResponse {
  MyPosPluginResponse({
    this.methodName,
    this.status,
    this.message,
  });

  MyPosPluginResponse.fromMap(Map<dynamic, dynamic> response) {
    methodName = response['methodName'];
    status = response['status'];
    message = response['message'];
  }

  String methodName;
  bool status;
  Map<dynamic, dynamic> message;

  String toString() {
    return 'Method: $methodName, status: $status, message: $message';
  }
}
