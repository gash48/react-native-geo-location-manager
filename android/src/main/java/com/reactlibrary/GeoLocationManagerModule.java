package com.reactlibrary;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import com.facebook.react.modules.core.DeviceEventManagerModule;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.bridge.Promise;

public class GeoLocationManagerModule extends ReactContextBaseJavaModule {
    private final ReactApplicationContext reactContext;

    public GeoLocationManagerModule(ReactApplicationContext reactContext) {
        super(reactContext);
        this.reactContext = reactContext;

        BroadcastReceiver geoLocationReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Location message = intent.getParcelableExtra("message");
                GeoLocationManagerModule.this.sendEvent(message);
            }
        };
        LocalBroadcastManager.getInstance(getReactApplicationContext()).registerReceiver(geoLocationReceiver, new IntentFilter(Constants.MESSAGE_INTENT));
    }

    @Override
    public String getName() {
        return Constants.SERVICE_NAME;
    }

    @ReactMethod
    public void startService(Promise promise) {
        try {
            Intent intent = new Intent(Constants.SERVICE_INTENT);
            intent.setClass(this.getReactApplicationContext(), GeoLocationManager.class);
            getReactApplicationContext().startService(intent);
        } catch (Exception e) {
            promise.reject(e);
            return;
        }
        promise.resolve("Geolocation Service Started");
    }

    @ReactMethod
    public void stopService(Promise promise) {
        try {
            Intent intent = new Intent(Constants.SERVICE_INTENT);
            intent.setClass(this.getReactApplicationContext(), GeoLocationManager.class);
            this.getReactApplicationContext().stopService(intent);
        } catch (Exception e) {
            promise.reject(e);
            return;
        }
        promise.resolve("Geolocation Service Stopped");
    }

    private void sendEvent(Location message) {
        WritableMap map = Arguments.createMap();
        WritableMap coordMap = Arguments.createMap();
        coordMap.putDouble("latitude", message.getLatitude());
        coordMap.putDouble("longitude", message.getLongitude());
        coordMap.putDouble("accuracy", message.getAccuracy());
        coordMap.putDouble("altitude", message.getAltitude());
        coordMap.putDouble("heading", message.getBearing());
        coordMap.putDouble("speed", message.getSpeed());

        map.putMap("coords", coordMap);
        map.putDouble("timestamp", message.getTime());

        getReactApplicationContext().getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class).emit(Constants.LOCATION_EVENT, map);
    }
}
