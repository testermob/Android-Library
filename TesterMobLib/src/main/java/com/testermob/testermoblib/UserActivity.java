package com.testermob.testermoblib;

public class UserActivity {
    private String pakage_name;
    private String gmail;
    private String days;
    private String hours;
    private String minutes;
    private String seconds;
    private boolean installed;
    private String activities;
    private String clicks;
    private String os_version;
    private String os_name;
    private String app_id;
    private String app_version;

    public UserActivity(String appId) {
        this.pakage_name = "";
        this.gmail = "";
        this.days = "";
        this.hours = "";
        this.minutes = "";
        this.seconds = "";
        this.installed = false;
        this.activities = "";
        this.clicks = "0";
        this.os_version = "";
        this.os_name = "";
        this.app_id = appId;
        this.app_version = "";
    }

    // Setters
    public void setPakageName(String pakageName) {
        this.pakage_name = pakageName;
    }
    public void setGmail(String gmail) { this.gmail = gmail; }
    public void setDays(String days) {
        this.days = days;
    }
    public void setHours(String hours) {
        this.hours = hours;
    }
    public void setMinutes(String minutes) {
        this.minutes = minutes;
    }
    public void setSeconds(String seconds) {
        this.seconds = seconds;
    }
    public void setInstalled(boolean installed) { this.installed = installed; }
    public void setActivity(String activities) {this.activities = activities; }
    public void setClicks(String clicks) { this.clicks = clicks; }
    public void setOsVersion(String os_version) {
        this.os_version = os_version;
    }
    public void setOsName(String os_name) { this.os_name = os_name; }
    public void setAppVersion(String app_version) { this.app_version = app_version; }
}