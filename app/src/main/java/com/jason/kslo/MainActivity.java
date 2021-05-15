package com.jason.kslo;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import com.jason.kslo.Install.CheckUpdateTask;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar actionBar = getSupportActionBar();
        if (!(actionBar == null)) {
            actionBar.setTitle(R.string.app_name);
        }
        Button downloadNow = findViewById(R.id.downloadBtn);
        downloadNow.setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            //noinspection deprecation
            builder.setTitle(R.string.DownloadApp)
                    .setMessage(R.string.DownloadAppEnquiry)
                    .setNegativeButton(R.string.Cancel, (dialogInterface, i) -> {

                    })
                    .setPositiveButton(R.string.Confirm, (dialogInterface, i) ->
                            new CheckUpdateTask(MainActivity.this, true).execute())
                    .show();
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actionbar_files_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_action_bar_settings) {
            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            finish();
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}