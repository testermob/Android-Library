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



### 3. Add Required Libraries to build.gradle
Add the following libraries to your build.gradle file:

implementation 'com.squareup.retrofit2:retrofit:2.9.0'
implementation 'com.squareup.retrofit2:converter-gson:2.9.0'


## AndroidManifest Permissions

Before using the TesterMob library, you need to add the necessary permissions in your `AndroidManifest.xml` file.

In your `AndroidManifest.xml` file, add the following permissions:

```xml
<uses-permission android:name="android.permission.INTERNET"/>
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>



### Main Activity Integration
To use the tracker, implement it in your MainActivity class.

### 1. Declare the Tracker in MainActivity
In the MainActivity class, add the following code before the onCreate() method:

public static UserActivityTracker tracker;
public String appId = "app_id"; // copy your app id from dashboard


### 2. Initialize the Tracker in onCreate()
Inside the onCreate() method, add the following code:

tracker = new UserActivityTracker(this, appId);
tracker.sendUserActivity(appId);
tracker.getTesterMob();


### Lifecycle Methods: onResume and onStop
These methods are essential for tracking user activity in the app.

### 1. onResume Method
In the MainActivity, add the following method:

@Override
protected void onResume() {
    super.onResume();
    tracker.sendUserActivity(appId);
}

### 2. onStart and onStop Methods
In the MainActivity, add the following methods:

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


### Other Activities Integration
To track user activity in other activities, implement the tracker as follows:

1. onStart and onStop in Other Activities
In other activities, also add the following code before the onCreate() method:

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




