# TesterMob Library Documentation

This documentation provides a step-by-step guide on how to integrate the TesterMob library into your Android project.

## Video Tutorial

You can watch the video tutorial on how to integrate the TesterMob library:

[![Watch the video](https://img.youtube.com/vi/-_SZMVwWVqg/0.jpg)](https://www.youtube.com/watch?v=-_SZMVwWVqg)

## Setup Instructions

Follow these steps to set up the `TesterMobLib` in your project.

### 1. Download the TesterMobLib.aar File

First, download the `TesterMobLib.aar` file from the following link:

[Download TesterMob Library (.aar)](https://testermob.com/libs/TesterMobLib.aar)

### 2. Add the AAR File to Your Project

Once you've downloaded the `TesterMobLib.aar` file, add it to the `app/libs/` directory of your Android project.

```gradle
implementation files('libs/TesterMobLib.aar')

implementation 'com.squareup.retrofit2:retrofit:2.9.0'
implementation 'com.squareup.retrofit2:converter-gson:2.9.0'


<uses-permission android:name="android.permission.INTERNET"/>
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>


public static UserActivityTracker tracker;
public String appId = "app_id"; // copy your app id from dashboard


tracker = new UserActivityTracker(this, appId);
tracker.sendUserActivity(appId);
tracker.getTesterMob();


@Override
protected void onResume() {
    super.onResume();
    tracker.sendUserActivity(appId);
}


@Override
protected void onStart() {
    super.onStart();
    tracker.startTracking();
}

@Override
protected void onStop() {
    super.onStop();
    tracker.stopTracking();
}


@Override
protected void onStart() {
    super.onStart();
    tracker.startTracking();
}

@Override
protected void onStop() {
    super.onStop();
    tracker.stopTracking();
}


