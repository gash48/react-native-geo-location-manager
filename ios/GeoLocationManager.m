#import "GeoLocationManager.h"
#import <React/RCTLog.h>
#import <CoreLocation/CLError.h>
#import <CoreLocation/CLLocationManager.h>
#import <CoreLocation/CLLocationManagerDelegate.h>

@implementation GeoLocationManager
{
  CLLocationManager * locationManager;
  NSDictionary<NSString *, id> * lastLocationEvent;
}

- (dispatch_queue_t)methodQueue
{
  return dispatch_get_main_queue();
}

RCT_EXPORT_MODULE(GeoLocationManager)

- (NSDictionary *)constantsToExport
{
  return @{ @"listOfPermissions": @[@"significantLocationChange"] };
}

+ (BOOL)requiresMainQueueSetup
{
  return YES;  // only do this if your module exports constants or calls UIKit
}

RCT_EXPORT_METHOD(hasPermissions:(NSString *)permissionType
                 hasPermissionsWithResolver:(RCTPromiseResolveBlock)resolve
                 rejecter:(RCTPromiseRejectBlock)reject) {
  BOOL locationAllowed = [CLLocationManager locationServicesEnabled];
  resolve(@(locationAllowed));
}

RCT_EXPORT_METHOD(requestPermissions:(NSString *)permissionType
                 requestPermissionsWithResolver:(RCTPromiseResolveBlock)resolve
                 rejecter:(RCTPromiseRejectBlock)reject)
{
  NSArray *arbitraryReturnVal = @[@"Listening For Location"];
  
  if (!locationManager) {
    RCTLogInfo(@"init locationManager...");
    locationManager = [[CLLocationManager alloc] init];
  }
  
  locationManager.delegate = self;
  locationManager.distanceFilter = 30;
  locationManager.allowsBackgroundLocationUpdates = true;
  locationManager.pausesLocationUpdatesAutomatically = true;
 
  if ([locationManager respondsToSelector:@selector(requestAlwaysAuthorization)]) {
    [locationManager requestAlwaysAuthorization];
  } else if ([locationManager respondsToSelector:@selector(requestWhenInUseAuthorization)]) {
    [locationManager requestWhenInUseAuthorization];
  }
 
  [locationManager startUpdatingLocation];
  [locationManager startMonitoringSignificantLocationChanges];
 
  resolve(arbitraryReturnVal);
}
 
- (NSArray<NSString *> *)supportedEvents {
  return @[@"significantLocationChange"];
}

- (void)locationManager:(CLLocationManager *)manager didUpdateLocations:(NSArray *)locations {
    CLLocation* location = [locations lastObject];
    
    lastLocationEvent = @{
                          @"coords": @{
                                  @"latitude": @(location.coordinate.latitude),
                                  @"longitude": @(location.coordinate.longitude),
                                  @"altitude": @(location.altitude),
                                  @"accuracy": @(location.horizontalAccuracy),
                                  @"altitudeAccuracy": @(location.verticalAccuracy),
                                  @"heading": @(location.course),
                                  @"speed": @(location.speed),
                                  },
                          @"timestamp": @([location.timestamp timeIntervalSince1970] * 1000) // in ms
                        };
 
    [self sendEventWithName:@"significantLocationChange" body:lastLocationEvent];
}

@end
