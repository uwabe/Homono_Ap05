package com.websarva.wings.android.homono_ap05;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.SettingsClient;

import java.net.URI;
import java.net.URL;
import java.text.DateFormat;
import java.util.Date;

public class MainMenu extends AppCompatActivity {

    private double longitude = 0; //経度
    private double latitude = 0; //緯度
//    private final int REQUEST_PERMISSION = 10;
//    // Fused Location Provider API.
//    private FusedLocationProviderClient fusedLocationClient= LocationServices.getFusedLocationProviderClient(this);
//    private String lastUpdateTime;
//    private Boolean requestingLocationUpdates;
//    private static final int REQUEST_CHECK_SETTINGS = 0x1;
//    private int priority = 0;
//
//    // Location Settings APIs.
//    private SettingsClient settingsClient= LocationServices.getSettingsClient(this);
//    private LocationSettingsRequest locationSettingsRequest;
//    private LocationCallback locationCallback;
//    private LocationRequest locationRequest;
//    private Location location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        //現在地ボタン設定
        Button menubtn1 =findViewById(R.id.button);
        MainMenu.CurrentLocationListener cll = new CurrentLocationListener();
        menubtn1.setOnClickListener(cll);

        //お気に入りボタン設定
        Button menubtn2 =findViewById(R.id.button2);
        MainMenu.FavoriteSearchListener fsl = new FavoriteSearchListener();
        menubtn2.setOnClickListener(fsl);

        //駅検索ボタン設定
        Button menubtn3 =findViewById(R.id.button3);
        MainMenu.StationSearchListener ssl = new StationSearchListener();
        menubtn3.setOnClickListener(ssl);

        //位置情報取得
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        //位置情報をキャッチするリスナ
        GPSLocationListener locationListener = new GPSLocationListener();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // ACCESS_FINE_LOCATIONの許可を求めるダイアログを表示。その後、リクエストコードを1000に設定
            String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION};
            ActivityCompat.requestPermissions(MainMenu.this,permissions,1000);
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
//        if(Build.VERSION.SDK_INT >= 23){
//            checkPermission();
//        }
//        else{
//        }
    }

//    // 位置情報許可の確認
//    public void checkPermission() {
//        // 既に許可している
//        if (ActivityCompat.checkSelfPermission(this,
//                Manifest.permission.ACCESS_FINE_LOCATION)==
//                PackageManager.PERMISSION_GRANTED){
//        }
//        // 拒否していた場合
//        else{
//            requestLocationPermission();
//        }
//    }
//
//    // 許可を求める
//    private void requestLocationPermission() {
//        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
//                Manifest.permission.ACCESS_FINE_LOCATION)) {
//            ActivityCompat.requestPermissions(this,
//                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
//                    REQUEST_PERMISSION);
//
//        } else {
//            Toast toast = Toast.makeText(this,
//                    "許可されないとアプリが実行できません", Toast.LENGTH_SHORT);
//            toast.show();
//
//            ActivityCompat.requestPermissions(this,
//                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION,},
//                    REQUEST_PERMISSION);
//
//        }
//    }
//    // 結果の受け取り
//    @Override
//    public void onRequestPermissionsResult(
//            int requestCode,
//            @NonNull String[] permissions,
//            @NonNull int[] grantResults) {
//
//        if (requestCode == REQUEST_PERMISSION) {
//            // 使用が許可された
//            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//
//            } else {
//                // それでも拒否された時の対応
//                Toast toast = Toast.makeText(this,
//                        "このアプリは実行できません", Toast.LENGTH_SHORT);
//                toast.show();
//            }
//        }
//    }

    private class CurrentLocationListener implements View.OnClickListener{
        @Override
        public void onClick(View view){
            //priority = 0;
            //createLocationCallback();
            //createLocationRequest();
            //buildLocationSettingsRequest();
            // 検索結果表示画面へ遷移
            Intent intent = new Intent(MainMenu.this, SearchResultActivity.class);
            String lon = String.valueOf(longitude);
            String lat = String.valueOf(latitude);
            intent.putExtra("station_lon",lon);
            intent.putExtra("station_lat",lat);
            //String urlStr = "geo:" + latitude + "," + longitude;
            //画面パラメータと一緒に渡す？
            Log.i("ButtonClick","現在地押したー");
            Log.i("ButtonClick1","現在地:"+ longitude+ "&" + latitude);
            startActivity(intent);
        }
    }
//    // locationのコールバックを受け取る
//    private void createLocationCallback() {
//        locationCallback = new LocationCallback() {
//            @Override
//            public void onLocationResult(LocationResult locationResult) {
//                super.onLocationResult(locationResult);
//
//                location = locationResult.getLastLocation();
//                updateLocationUI();
//            }
//        };
//    }
//    private void updateLocationUI() {
//        // getLastLocation()からの情報がある場合のみ
//        if (location != null) {
//            longitude = location.getLongitude();
//            latitude = location.getLatitude();
//            //String fusedName[] = {
//            //        "Latitude", "Longitude"
//            //};
//            //double fusedData[] = {
//            //        location.getLatitude(),
//            //        location.getLongitude()
//            //};
//        }
//    }
//    private void createLocationRequest() {
//        locationRequest = new LocationRequest();
//
//        if (priority == 0) {
//            // 高い精度の位置情報を取得したい場合
//            // インターバルを例えば5000msecに設定すれば
//            // マップアプリのようなリアルタイム測位となる
//            // 主に精度重視のためGPSが優先的に使われる
//            locationRequest.setPriority(
//                    LocationRequest.PRIORITY_HIGH_ACCURACY);
//
//        } else if (priority == 1) {
//            // バッテリー消費を抑えたい場合、精度は100mと悪くなる
//            // 主にwifi,電話網での位置情報が主となる
//            // この設定の例としては　setInterval(1時間)、setFastestInterval(1分)
//            locationRequest.setPriority(
//                    LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
//
//        } else if (priority == 2) {
//            // バッテリー消費を抑えたい場合、精度は10kmと悪くなる
//            locationRequest.setPriority(
//                    LocationRequest.PRIORITY_LOW_POWER);
//
//        } else {
//            // 受け身的な位置情報取得でアプリが自ら測位せず、
//            // 他のアプリで得られた位置情報は入手できる
//            locationRequest.setPriority(
//                    LocationRequest.PRIORITY_NO_POWER);
//        }
//
//        // アップデートのインターバル期間設定
//        // このインターバルは測位データがない場合はアップデートしません
//        // また状況によってはこの時間よりも長くなることもあり
//        // 必ずしも正確な時間ではありません
//        // 他に同様のアプリが短いインターバルでアップデートしていると
//        // それに影響されインターバルが短くなることがあります。
//        // 単位：msec
//        locationRequest.setInterval(60000);
//        // このインターバル時間は正確です。これより早いアップデートはしません。
//        // 単位：msec
//        locationRequest.setFastestInterval(5000);
//    }
//
//    // 端末で測位できる状態か確認する。wifi, GPSなどがOffになっているとエラー情報のダイアログが出る
//    private void buildLocationSettingsRequest() {
//        LocationSettingsRequest.Builder builder =
//                new LocationSettingsRequest.Builder();
//
//        builder.addLocationRequest(locationRequest);
//        locationSettingsRequest = builder.build();
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode,
//                                    int resultCode, Intent data) {
//        switch (requestCode) {
//            // Check for the integer request code originally supplied to startResolutionForResult().
//            case REQUEST_CHECK_SETTINGS:
//                switch (resultCode) {
//                    case Activity.RESULT_OK:
//                        Log.i("debug", "User agreed to make required location settings changes.");
//                        break;
//                    case Activity.RESULT_CANCELED:
//                        Log.i("debug", "User chose not to make required location settings changes.");
//                        requestingLocationUpdates = false;
//                        break;
//                }
//                break;
//        }
//    }



    private class FavoriteSearchListener implements View.OnClickListener{
        @Override
        public void onClick(View view){
            // お気に入り画面へ遷移
            Intent intent = new Intent(MainMenu.this,FavoriteSearch.class);
            startActivity(intent);
        }
    }

    private class StationSearchListener implements View.OnClickListener{
        @Override
        public void onClick(View view){
            // 駅名指定検索画面へ遷移
            Intent intent = new Intent(MainMenu.this,StationSearch.class);
            startActivity(intent);
        }
    }


    private class GPSLocationListener implements LocationListener {
        @Override
        public void onLocationChanged(Location location){
            //引数のLocationオブジェクトから緯度を取得
            latitude = location.getLatitude();
            //引数のLocationオブジェクトから経度を取得
            longitude = location.getLongitude();

            Log.i("ButtonClick2","現在地:"+ longitude+ "&" + latitude);
        }
        @Override
        public void onStatusChanged(String provider, int status, Bundle extras){}
        @Override
        public void onProviderEnabled(String provider){}
        @Override
        public void onProviderDisabled(String provider){}
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[]permissions, int[]grantResults){
        //パーミッションダイアログで許可を選択
        if(requestCode == 1000 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            //Locationオブジェクトを取得
            LocationManager locationManager = (LocationManager)getSystemService(LOCATION_SERVICE);
            //位置情報が更新された際のリスナオブジェクトを生成
            GPSLocationListener locationListener = new GPSLocationListener();
            //再度許可が下りていないかチェックし、下りていなければ処理を中止
            if(ActivityCompat.checkSelfPermission(MainMenu.this,Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
                return;
            }
            //位置情報の追跡を開始
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);
        }
    }
}
