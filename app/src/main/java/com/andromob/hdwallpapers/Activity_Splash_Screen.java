package com.andromob.hdwallpapers;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.animation.AlphaAnimation;
import android.widget.TextView;
import android.widget.Toast;

import com.andromob.hdwallpapers.func.DataUrl;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static android.support.v4.app.ActivityCompat.finishAffinity;


/**
 * Created by andromob on 26/08/2018.
 */

public class Activity_Splash_Screen extends AppCompatActivity {

    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
    protected static List<DataUrl> data = new ArrayList<>();
    private final String ourDataFilenameNoSlash = "wallpaperhd.json";
    private final String ourDataFilename = "/" + ourDataFilenameNoSlash;

    AlphaAnimation alpha;
    TextView splashText, poweredbyText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);


        alpha= new AlphaAnimation(0, 1);
        alpha.setDuration(2500);
        poweredbyText = (TextView)findViewById(R.id.poweredby);
        int year = Calendar.getInstance().get(Calendar.YEAR);
        poweredbyText.setText("Copyright © "+ year + " " + getString(R.string.dev));
        splashText = (TextView)findViewById(R.id.text_splash);
        splashText.setAnimation(alpha);



        if(isOnline()){
            new AsyncFetch().execute();
        }
        else{


            // alert dialog when api < 24
            new AlertDialog.Builder(this).setTitle("Check Internet Connection")
                    .setMessage("No internet connetion available! Activate it, then click refresh.")
                    .setCancelable(false)
                    .setPositiveButton("Exit",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                        finishAffinity();
                                    }
                                    System.exit(1);
                                }
                            })
                    .setNegativeButton("Refresh", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startActivity(new Intent(Activity_Splash_Screen.this, Activity_Splash_Screen.class));

                        }
                    })
                    .create()
                    .show();
        }

    }

    //check internet connexion
    protected boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnected()) {
            return true;
        } else {
            return false;
        }
    }

    private boolean isNetworkAvailable(Context context) {
        int[] networkTypes = {ConnectivityManager.TYPE_MOBILE,
                ConnectivityManager.TYPE_WIFI};
        try {
            ConnectivityManager connectivityManager =
                    (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            for (int networkType : networkTypes) {
                NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
                if (activeNetworkInfo != null &&
                        activeNetworkInfo.getType() == networkType)
                    return true;
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }

    private void writeJsonToFile(String data, Context context) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput(ourDataFilenameNoSlash, Context.MODE_PRIVATE));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
            Log.i("io", "Wrote file");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected String buffToString(Reader ourReader, boolean save) {
        try {
            BufferedReader reader = new BufferedReader(ourReader);
            StringBuilder result = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                result.append(line);
            }

            // write it to ourdata.json
            if (save) {
                if (!result.toString().equals(null)) {
                    writeJsonToFile(result.toString(), Activity_Splash_Screen.this);
                }
            }

            return (result.toString());
        } catch (IOException e) {
            e.printStackTrace();
            return e.toString();
        }
    }

    private class AsyncFetch extends AsyncTask<String, String, String> {

        HttpURLConnection connection;
        URL url = null;

        @Override
        protected String doInBackground(String... strings) {

            if (isNetworkAvailable(Activity_Splash_Screen.this)) {
                try {
                    url = new URL(Classe_Settings.wallpaperDataBase);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                    return e.toString();
                }

                try {
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setReadTimeout(READ_TIMEOUT);
                    connection.setConnectTimeout(CONNECTION_TIMEOUT);
                    connection.setRequestMethod("GET");

                } catch (IOException e1) {
                    e1.printStackTrace();
                    return e1.toString();
                }

                try {
                    int responseCode = connection.getResponseCode();

                    if (responseCode == HttpURLConnection.HTTP_OK) {

                        InputStream inputStream = connection.getInputStream();
                        return buffToString(new InputStreamReader(inputStream), true);

                    } else {
                        File ourData = new File(Activity_Splash_Screen.this.getFilesDir().getPath() + ourDataFilename);
                        if (ourData.exists()) {
                            return buffToString(new FileReader(ourData), false);
                        } else {
                            Toast.makeText(Activity_Splash_Screen.this, "Cannot connect to server, please reopen app and try again.", Toast.LENGTH_SHORT).show();
                            return ("unsuccessful");
                        }
                    }
                } catch (IOException e2) {
                    e2.printStackTrace();
                    return e2.toString();
                } finally {
                    connection.disconnect();
                }
            } else {
                File ourData = new File(Activity_Splash_Screen.this.getFilesDir().getPath() + ourDataFilename);
                try {
                    return buffToString(new FileReader(ourData), false);

                } catch (IOException e) {
                    e.printStackTrace();
                    return e.toString();
                }
            }
        }

        @Override
        protected void onPostExecute(String result) {
            try {
                JSONArray jsonArray = new JSONArray(result);

                // Extract data from json and store into ArrayList as class objects
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonData = jsonArray.getJSONObject(i);
                    DataUrl dataUrl = new DataUrl();
                    dataUrl.wallIndex = jsonData.getInt("wallpaper_index");
                    dataUrl.wallName = jsonData.getString("wallpaper_name");
                    dataUrl.wallSite = jsonData.getString("wallpaper_site_name");
                    dataUrl.wallSiteUrl = jsonData.getString("wallpaper_site_url");
                    dataUrl.wallURL = jsonData.getString("wallpaper_url");
                    data.add(dataUrl);
                    Intent intent = new Intent(Activity_Splash_Screen.this, MainActivity.class);
                    startActivity(intent);
                }


            } catch (JSONException e) {
                Toast.makeText(Activity_Splash_Screen.this, e.toString(), Toast.LENGTH_LONG).show();
            }

        }
    }
}
