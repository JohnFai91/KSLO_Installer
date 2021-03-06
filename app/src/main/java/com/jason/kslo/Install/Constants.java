package com.jason.kslo.Install;

class Constants {


    // json {"url":"http://192.168.205.33:8080/Hello/app_v3.0.1_Other_20150116.apk","versionCode":2,"updateMessage":"版本更新信息"}

    static final String APK_DOWNLOAD_URL = "url";
    static final String APK_DOWNLOAD_URL_x86 = "url_x86";
    static final String APK_DOWNLOAD_URL_x86_64 = "url_x86_64";
    static final String APK_DOWNLOAD_URL_arm = "url_arm";
    static final String APK_DOWNLOAD_URL_arm64 = "url_arm64";

    static final String TAG = "UpdateChecker";

    static final String UPDATE_URL = "https://github.com/JohnFai91/com.jason.kslo/raw/master/update.json";
}
