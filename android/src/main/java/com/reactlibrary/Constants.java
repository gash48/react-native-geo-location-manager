package com.reactlibrary;

public class Constants {
  public static final String SERVICE_NAME = "GeoLocationManager";
  public static final String SERVICE_INTENT = "ForegroundGeoLocation";
  public static final String MESSAGE_INTENT = "GeoLocationUpdate";
  public static final String LOCATION_EVENT = "updateLocation";

  public static final String NOTIFICATION_CHANNEL_ID = "ForegroundGeoSvc";
  public static final String NOTIFICATION_CHANNEL_NAME = "Foreground Service Channel";
  public static final String NOTIFICATION_TITLE = "Ninja Tasks";
  public static final String NOTIFICATION_CONTENT = "Is using your location in the background"; 

  public static int NOTIFICATION_ID = 6991;
  public static int MIN_TIME_MS = 5000;
  public static int MIN_DISTANCE_M = 30;
  public static int PINTENT_REQUESTCODE = 1000;
}
