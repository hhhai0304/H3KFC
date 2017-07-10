package vn.name.hohoanghai.h3kfc;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import java.io.File;
import java.util.ArrayList;
import java.util.Locale;

import vn.name.hohoanghai.model.Category;
import vn.name.hohoanghai.model.MainGSON;
import vn.name.hohoanghai.model.Product;
import vn.name.hohoanghai.util.ConnectionChecker;
import vn.name.hohoanghai.util.DatabaseHelper;

public class SplashScreenActivity extends AppCompatActivity {

    int downloadCount, maxItem;
    String serverVersion;
    DatabaseHelper databaseHelper;

    TextView txtProgress;
    RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
        }

        updateLanguage();
        addControls();
        addEvents();
    }

    private void updateLanguage() {
        SharedPreferences preferences = getSharedPreferences(getResources().getString(R.string.language_file), MODE_PRIVATE);
        String language = preferences.getString("language", "vi");
        String country = preferences.getString("country", "VN");

        Resources resources = getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        android.content.res.Configuration configuration = resources.getConfiguration();
        configuration.locale = new Locale(language, country);
        resources.updateConfiguration(configuration, metrics);
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(receiverDownloadComplete, filterDownloadComplete);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(receiverDownloadComplete != null)
            unregisterReceiver(receiverDownloadComplete);
    }

    @SuppressLint("SetTextI18n")
    private void addControls() {
        downloadCount = 0;
        databaseHelper = new DatabaseHelper(this);

        txtProgress = (TextView) findViewById(R.id.txtProgress);
        txtProgress.setText(getResources().getString(R.string.checking));

        queue = Volley.newRequestQueue(this);
    }

    private void addEvents() {
        versionChecker();
    }

    /**
     * Compare server data with device data
     */
    private void versionChecker() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, getResources().getString(R.string.getversion), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                serverVersion = response;
                if(!getCurrentVersion().equals(serverVersion)) {
                    txtProgress.setText(getResources().getString(R.string.downloading) + " 0%");
                    importNewData();
                } else {
                    Intent i = new Intent(SplashScreenActivity.this, MainActivity.class);
                    startActivity(i);
                    finish();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ConnectionChecker connectionChecker = new ConnectionChecker(SplashScreenActivity.this);
                connectionChecker.createNetworkStateDialog();
            }
        });
        queue.add(stringRequest);
    }

    /**
     * Download new server data to user's device
     */
    private void importNewData() {
        databaseHelper.clearAllProducts();
        SharedPreferences preferences = getSharedPreferences(getResources().getString(R.string.category_file), MODE_PRIVATE);
        preferences.edit().clear().apply();

        File direct = new File(String.valueOf(getExternalFilesDir(null)));
        if (!direct.exists()) {
            direct.mkdirs();
        } else {
            for (File child : direct.listFiles()) {
                child.delete();
            }
        }

        StringRequest stringRequestCategory = new StringRequest(Request.Method.POST, getResources().getString(R.string.get_category), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                createCategory(response);
            }
        }, null);

        StringRequest stringRequestDishes = new StringRequest(Request.Method.POST, getResources().getString(R.string.getallproducts), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                createDatabase(response);
            }
        }, null);

        queue.add(stringRequestCategory);
        queue.add(stringRequestDishes);
    }

    /**
     * Save all Categories to SharedPreferences XML file
     * @param response JSON string
     */
    private void createCategory(String response) {
        SharedPreferences preferences = getSharedPreferences(getResources().getString(R.string.category_file), MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        MainGSON gson = new Gson().fromJson(response, MainGSON.class);

        ArrayList<Category> categories = gson.getCategories();
        editor.putInt("size", categories.size());
        for(int i = 0; i < categories.size(); i++) {
            editor.putString("categoryID" + i, categories.get(i).getCategoryID());
            editor.putString("name" + i, categories.get(i).getName());
            editor.putString("detail" + i, categories.get(i).getDetail());
        }
        editor.apply();
    }

    /**
     * Add all Dishes to device's database
     * @param response JSON string
     */
    private void createDatabase(String response) {
        MainGSON gson = new Gson().fromJson(response, MainGSON.class);

        ArrayList<Product> products = gson.getProducts();
        setVersion(serverVersion);
        maxItem = products.size();

        for(int i = 0; i < products.size(); i++) {
            downloadProductImage(getResources().getString(R.string.productAvatar) + products.get(i).getAvatarUrl());
            products.get(i).setAvatar("file://" + String.valueOf(getExternalFilesDir(null)) + "/" + products.get(i).getAvatarUrl());
            databaseHelper.addNewProduct(products.get(i));
        }
    }

    /**
     * Download dish image to user's device
     * @param url Image url
     */
    private void downloadProductImage(String url) {
        DownloadManager downloadManager = (DownloadManager) this.getSystemService(DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(url);
        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_HIDDEN);
        request.setDestinationInExternalFilesDir(this, "", uri.getLastPathSegment());

        downloadManager.enqueue(request);
    }

    /**
     * Event when download finish
     */
    IntentFilter filterDownloadComplete = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
    BroadcastReceiver receiverDownloadComplete = new BroadcastReceiver() {
        @SuppressLint("SetTextI18n")
        @Override
        public void onReceive(Context context, Intent intent) {
            downloadCount++;
            int currentProgress = (downloadCount * 100) / maxItem;
            txtProgress.setText(getResources().getString(R.string.downloading) + " " + currentProgress + "%");
            if(downloadCount == maxItem) {
                Intent i = new Intent(SplashScreenActivity.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        }
    };

    /**
     * Set new database version to user's device
     * @param version New version string
     */
    private void setVersion(String version) {
        SharedPreferences preferences = getSharedPreferences(getResources().getString(R.string.version_file), MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("version", version);
        editor.apply();
    }

    /**
     * Get current database version on user's device
     * @return Version string
     */
    private String getCurrentVersion() {
        SharedPreferences preferences = getSharedPreferences(getResources().getString(R.string.version_file), MODE_PRIVATE);
        String version = preferences.getString("version", "");
        if(version.isEmpty()) {
            setVersion("0");
        }
        return version;
    }
}