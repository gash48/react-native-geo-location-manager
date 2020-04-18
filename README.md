# react-native-geo-location-manager

React Native Geolocation Manager is a Cross Compatible Native Module that enables Geolocation tracking for both Android & IOS. Even when the app is closed.<br />
Behind the scenes this module employs Significant Location Changes (For IOS) and a Foreground Location Service (For Android)

## Important Note 

For React Native >= 0.60 The Module Can be Automatically Linked<br />
For React Native < 0.60, The Module has to be manually Linked. Please Refer to React Native Docs

## Installation & Requisites

`$ yarn add https://github.com/gash48/react-native-geo-location-manager.git`<br />
or <br />
`$ npm install https://github.com/gash48/react-native-geo-location-manager.git --save`


### IOS 
`$ cd ios && pod install`

For Permissions, make sure 'NSLocationAlwaysAndWhenInUseUsageDescription', 'NSLocationAlwaysUsageDescription', 'NSLocationWhenInUseUsageDescription' exists in Info.plist.<br />
This Module However relies on "Location Always" as it works in the background 

### Android
In AndroidManifest.xml, make sure ure app contains<br />
`$ <uses-permission android:name="android.permission.FOREGROUND_SERVICE"/>` --- For Foreground Service Permission <br />
`$ <service android:name="com.reactlibrary.GeoLocationManager"/>` ---- Foreground Service of The Module

## Usage

The Module is to be used differently for both IOS and Android

### IOS
```javascript
import { NativeModules, NativeEventEmitter } from "react-native";

const locationSvc = NativeModules.GeoLocationManager;
const EVENT_EMITTER = new NativeEventEmitter(locationSvc);

async requestPermissions() {
  const result = await locationSvc.requestPermissions("");
  return result;
}

requestPermissions();
EVENT_EMITTER.addListener(location.listOfPermissions[0], geoData => {
  console.log(geoData);
});
```

### ANDROID
```javascript
import { NativeModules, DeviceEventEmitter } from "react-native";

// ------- For Starting The Service --------- //
NativeModules.GeoLocationManager.startService().then(() => {
  DeviceEventEmitter.addListener("updateLocation", geoData => {
    console.log(geoData);
  });
});

// ------- For Stopping The Service --------- //
NativeModules.GeoLocationManager.stopService();
```

## Presented By Gash :metal: :sunglasses:
