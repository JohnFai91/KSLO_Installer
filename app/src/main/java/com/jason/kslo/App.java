package com.jason.kslo;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.text.TextUtils;
import androidx.appcompat.app.AppCompatDelegate;

import java.util.Locale;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        SharedPreferences prefs = getBaseContext().getSharedPreferences("MyPref",MODE_PRIVATE);
        String Theme = prefs.getString("theme","Follow System");

        switch (Theme) {
            case "Follow System":
                setTheme(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
                break;
            case "Day Mode":
                setTheme(AppCompatDelegate.MODE_NIGHT_NO);
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                break;
            case "Night Mode":
                setTheme(AppCompatDelegate.MODE_NIGHT_YES);
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                break;
        }
        updateLanguage(getApplicationContext());
    }
    public static void updateLanguage(Context ctx)
    {
        SharedPreferences prefs = ctx.getSharedPreferences("MyPref",MODE_PRIVATE);
        String lang = prefs.getString("lang","");
        Configuration cfg = new Configuration();
        if (!TextUtils.isEmpty(lang))
            if (lang.equals("zh-HK")){
                cfg.setLocale(Locale.TRADITIONAL_CHINESE);
                Locale.setDefault(Locale.TRADITIONAL_CHINESE);
                //noinspection deprecation
                ctx.getResources().updateConfiguration(cfg, null);
            } else if (lang.equals("en")){
                cfg.setLocale(Locale.ENGLISH);
                Locale.setDefault(Locale.ENGLISH);
                //noinspection deprecation
                ctx.getResources().updateConfiguration(cfg, null);
            } else {
                cfg.setLocale(Locale.getDefault());
                //noinspection deprecation
                ctx.getResources().updateConfiguration(cfg, null);
            }
    }

    public static void updateLanguage(Context ctx, String lang) {
        Configuration cfg = new Configuration();
        if (!TextUtils.isEmpty(lang))
            if (lang.equals("zh-HK")){
                cfg.setLocale(Locale.TRADITIONAL_CHINESE);
                Locale.setDefault(Locale.TRADITIONAL_CHINESE);
                //noinspection deprecation
                ctx.getResources().updateConfiguration(cfg, null);
            } else if (lang.equals("en")){
                Locale.setDefault(Locale.ENGLISH);
                cfg.setLocale(Locale.ENGLISH);
                //noinspection deprecation
                ctx.getResources().updateConfiguration(cfg, null);
            } else {
                cfg.setLocale(Locale.getDefault());
                //noinspection deprecation
                ctx.getResources().updateConfiguration(cfg, null);
            }
    }
}
