#import "MyposFlutterPlugin.h"
#if __has_include(<mypos_flutter/mypos_flutter-Swift.h>)
#import <mypos_flutter/mypos_flutter-Swift.h>
#else
// Support project import fallback if the generated compatibility header
// is not copied when this plugin is created as a library.
// https://forums.swift.org/t/swift-static-libraries-dont-copy-generated-objective-c-header/19816
#import "mypos_flutter-Swift.h"
#endif

@implementation MyposFlutterPlugin
+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar {
  [SwiftMyposFlutterPlugin registerWithRegistrar:registrar];
}
@end
