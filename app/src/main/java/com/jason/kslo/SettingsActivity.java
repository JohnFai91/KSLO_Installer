package com.jason.kslo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import java.util.ArrayList;
import java.util.List;


public class SettingsActivity extends AppCompatActivity {
    String Theme, Theme1, Theme2, Theme3;
    Spinner spinner;
    Button change2Eng, change2Chin;

    SharedPreferences pref;
    @SuppressLint("RestrictedApi")
    @Override
    @SuppressWarnings("ConstantConditions")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_settings);

        pref = getApplicationContext().getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        Theme = pref.getString("theme","Follow System");

        change2Eng =  findViewById(R.id.English);
        change2Chin =  findViewById(R.id.Chinese);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_back);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDefaultDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        setTitle(getString(R.string.Settings));

        Content content = new Content();
        content.run();
    }
    class Content implements Runnable{
        @Override
        public void run() {
            switch (Theme) {
                case "Day Mode":
                    Theme = getString(R.string.LightTheme);
                    Theme1 = getString(R.string.SystemDefault);
                    Theme2 = getString(R.string.LightTheme);
                    Theme3 = getString(R.string.DarkTheme);
                    break;
                case "Night Mode":
                    Theme = getString(R.string.DarkTheme);
                    Theme1 = getString(R.string.SystemDefault);
                    Theme2 = getString(R.string.LightTheme);
                    Theme3 = getString(R.string.DarkTheme);
                    break;
                default:
                    Theme = getString(R.string.SystemDefault);
                    Theme1 = getString(R.string.SystemDefault);
                    Theme2 = getString(R.string.LightTheme);
                    Theme3 = getString(R.string.DarkTheme);
                    break;
            }

            List<String> categories = new ArrayList<>();
            categories.add(0, Theme);
            categories.add(Theme1);
            categories.add(Theme2);
            categories.add(Theme3);

            // Set Spinner list items in array adapter..
            ArrayAdapter<String> SelectThemeSpinnerItemArrAdaptor = new ArrayAdapter<String>(SettingsActivity.this, android.R.layout.simple_spinner_item, categories){
            };

            SelectThemeSpinnerItemArrAdaptor.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            spinner = findViewById(R.id.SelectThemeSpinner);
            spinner.setAdapter(SelectThemeSpinnerItemArrAdaptor);

            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (position == 1) {
                        setTheme("Follow System");
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
                    }
                    else if(position == 2) {
                        setTheme("Day Mode");
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    }
                    else  if(position == 3){
                        setTheme("Night Mode");
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });


            change2Eng.setOnClickListener(v -> setLocale("en"));


            change2Chin.setOnClickListener(v -> setLocale("zh-HK"));
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void setLocale(String lang){
        Editor editor = pref.edit();

        editor.putString("lang", lang);  // Saving string

        // Save the changes in SharedPreferences
        editor.apply(); // commit changes

        finish();
        App.updateLanguage(this, lang);
        Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
        finish();
        startActivity(intent);
    }
    public void setTheme(String theme) {

        Editor editor = pref.edit();

        editor.putString("theme", theme);  // Saving string

        // Save the changes in SharedPreferences
        editor.apply(); // commit changes

        finish();
        Intent intent = new Intent(SettingsActivity.this, SettingsActivity.class);
        finish();
        startActivity(intent);
    }
}