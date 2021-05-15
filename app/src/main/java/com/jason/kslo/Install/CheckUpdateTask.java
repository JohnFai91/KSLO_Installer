package com.jason.kslo.Install;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import com.jason.kslo.R;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

@SuppressWarnings("deprecation")
@SuppressLint("StaticFieldLeak")
public
class CheckUpdateTask extends AsyncTask<Void, Void, String> {

    @SuppressWarnings("deprecation")
    private ProgressDialog dialog;
    @SuppressLint("StaticFieldLeak")
    private final Context mContext;
    private final boolean mShowProgressDialog;
    private static final String url = Constants.UPDATE_URL;
    private static final int BUFFER_SIZE = 10 * 1024; // 8k ~ 32K

    public CheckUpdateTask(Context context, boolean showProgressDialog) {

        this.mContext = context;
        this.mShowProgressDialog = showProgressDialog;

    }

    protected void onPreExecute() {
        if (mShowProgressDialog) {
            dialog = new ProgressDialog(mContext);
            dialog.setMessage(mContext.getString(R.string.update_dialog_checking));
            dialog.show();
        }
    }

    @Override
    protected void onPostExecute(String result) {

        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }

        if (!TextUtils.isEmpty(result)) {
            parseJson(result);
        }
    }

    private void parseJson(String result) {
        try {

            JSONObject obj = new JSONObject(result);

            String abi;

            String apkUrl;
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                abi = Build.CPU_ABI;
            } else {
                abi = Build.SUPPORTED_ABIS[0];
            }
            switch (abi){
                case "armeabi":
                case "armeabi-v7a":
                    apkUrl = obj.getString(Constants.APK_DOWNLOAD_URL_arm);
                    Log.d("CheckUpdate", "apk Architecture: " + "arm (32-bit)");
                    break;
                case "arm64-v8a":
                    apkUrl = obj.getString(Constants.APK_DOWNLOAD_URL_arm64);
                    Log.d("CheckUpdate", "apk Architecture: " + "arm64 (64-bit)");
                    break;
                case "x86":
                    apkUrl = obj.getString(Constants.APK_DOWNLOAD_URL_x86);
                    Log.d("CheckUpdate", "apk Architecture: " + "x86 (32-bit)");
                    break;
                case "x86_64":
                    apkUrl = obj.getString(Constants.APK_DOWNLOAD_URL_x86_64);
                    Log.d("CheckUpdate", "apk Architecture: " + "x86_64 (64-bit)");
                    break;
                default:
                    apkUrl = obj.getString(Constants.APK_DOWNLOAD_URL);
                    Log.d("CheckUpdate", "apk Architecture: " + "Unknown (using universal apk)");
                    break;
            }
        Content content = new Content();
        content.execute(apkUrl);

        } catch (JSONException e) {
            Log.e(Constants.TAG, "parse json error");
        }
    }

    @Override
    protected String doInBackground(Void... args) {
        Log.d("Testing", "Crashing: ");
        return HttpUtils.get(url);
    }

    class Content extends AsyncTask<String, String, String> {
        File apkFile;
        ProgressDialog pDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(mContext);
            pDialog.setMessage(mContext.getString(R.string.Downloading));
            pDialog.setIndeterminate(false);
            pDialog.setMax(100);
            pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... urlStr) {
            InputStream in = null;
            FileOutputStream out = null;
            try {
                URL url = new URL(urlStr[0]);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                urlConnection.setRequestMethod("GET");
                urlConnection.setDoOutput(false);
                urlConnection.setConnectTimeout(10 * 1000);
                urlConnection.setReadTimeout(10 * 1000);
                urlConnection.setRequestProperty("Connection", "Keep-Alive");
                urlConnection.setRequestProperty("Charset", "UTF-8");
                urlConnection.setRequestProperty("Accept-Encoding", "gzip, deflate");

                urlConnection.connect();
                in = urlConnection.getInputStream();
                File dir = mContext.getCacheDir();
                String apkName = urlStr[0].substring(urlStr[0].lastIndexOf("/") + 1);
                apkFile = new File(dir, apkName);
                out = new FileOutputStream(apkFile);
                byte[] buffer = new byte[BUFFER_SIZE];

                long total = 0;
                int byteRead;
                while ((byteRead = in.read(buffer)) != -1) {

                    total += byteRead;
                    // publishing the progress....
                    // After this onProgressUpdate will be called
                    publishProgress("" + (int) ((total * 100) / urlConnection.getContentLength()));

                    out.write(buffer, 0, byteRead);
                }


            } catch (Exception e) {
                Log.e(Constants.TAG, "download apk file error:" + e.getMessage());
            } finally {
                if (out != null) {
                    try {
                        out.close();
                    } catch (IOException ignored) {

                    }
                }
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException ignored) {

                    }
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            pDialog.dismiss();
            ApkUtils.installAPk(mContext, apkFile);
        }

        /**
         * Updating progress bar
         */
        protected void onProgressUpdate(String... progress) {
            // setting progress percentage
            pDialog.setProgress(Integer.parseInt(progress[0]));
        }
    }
}
