package com.reactlibrary;

import android.app.Service;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;

import android.Manifest;

import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;

import android.content.Intent;
import android.content.pm.PackageManager;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.core.content.ContextCompat;
import androidx.core.app.NotificationCompat;

import android.graphics.BitmapFactory;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;

import javax.annotation.Nullable;
import android.annotation.TargetApi;

public class GeoLocationManager extends Service {
  LocationManager locationManager = null;
  LocationListener locationListener = new LocationListener() {
    @Override
    public void onLocationChanged(Location location) {
      GeoLocationManager.this.sendMessage(location);
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {}

    @Override
    public void onProviderEnabled(String s) {}

    @Override
    public void onProviderDisabled(String s) {}
  };

  @Override
  @TargetApi(Build.VERSION_CODES.M)
  public void onCreate() {
    locationManager = getSystemService(LocationManager.class);

    int permissionCheck = ContextCompat.checkSelfPermission(this,
      Manifest.permission.ACCESS_FINE_LOCATION);
    if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
      locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, Constants.MIN_TIME_MS, Constants.MIN_DISTANCE_M, locationListener);
    }
  }

  private void sendMessage(Location location) {
    try {
      Intent intent = new Intent(Constants.MESSAGE_INTENT);
      intent.putExtra("message", location);
      LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public void onDestroy() {
    locationManager.removeUpdates(locationListener);
    super.onDestroy();
  }

  @Override
  public int onStartCommand(Intent intent, int flags, int startId) {
    createNotificationChannel();
    startForeground(Constants.NOTIFICATION_ID, getCompatNotification());
    return START_NOT_STICKY;
  }

  @Nullable
  @Override
  public IBinder onBind(Intent intent) {
    return null;
  }

  private void createNotificationChannel() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      NotificationChannel serviceChannel = new NotificationChannel(
        Constants.NOTIFICATION_CHANNEL_ID,
        Constants.NOTIFICATION_CHANNEL_NAME,
        NotificationManager.IMPORTANCE_DEFAULT
      );

      NotificationManager manager = getSystemService(NotificationManager.class);
      manager.createNotificationChannel(serviceChannel);
    }
  }

  private Notification getCompatNotification() {
    NotificationCompat.Builder builder = new NotificationCompat.Builder(this, Constants.NOTIFICATION_CHANNEL_ID);
    builder
      .setSmallIcon(R.drawable.ic_location)
      .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_stadium))
      .setContentTitle(Constants.NOTIFICATION_TITLE)
      .setContentText(Constants.NOTIFICATION_CONTENT)
      .setTicker(Constants.NOTIFICATION_CONTENT)
      .setWhen(System.currentTimeMillis());

    Intent startIntent = new Intent(getApplicationContext(), GeoLocationManager.class);
    PendingIntent contentIntent = PendingIntent.getActivity(this, Constants.PINTENT_REQUESTCODE, startIntent, 0);
    builder.setContentIntent(contentIntent);
    return builder.build();
  }

}