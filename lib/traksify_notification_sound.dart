import 'package:flutter/services.dart';

class TraksifyNotificationSound {
  static const MethodChannel _channel =
      MethodChannel('traksify_notification_sound');

  static Future<void> initialize() async {
    await _channel.invokeMethod('initialize');
  }
}
