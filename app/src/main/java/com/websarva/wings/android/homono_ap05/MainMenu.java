package com.websarva.wings.android.homono_ap05;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.net.URI;
import java.net.URL;

public class MainMenu extends AppCompatActivity {

    private double longitude = 0; //経度
    private double latitude = 0; //緯度

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
        /*//現在地
        Button menubtn1 = findViewById(R.id.button);
        menubtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //画面パラメーターおくる
                //GPS情報渡す
                //Intent intent = new Intent(MainMenu.this, 現在地.class);
                //startActivity(intent);
                Log.i("log1", "現在地が押されたー");
            }
        });

        //お気に入り
        Button menubtn2 = findViewById(R.id.button2);
        menubtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("log1", "お気に入りが押されたー");
                Intent intent = new Intent(MainMenu.this, FavoriteSearch.class);
                startActivity(intent);
            }
        });

        //駅
        //なーーぜーーかーーエラーーになるーーーはーーーー???Fragmentだからかーーー??????
        Button menubtn3 = findViewById(R.id.button3);
        menubtn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainMenu.this, StationSearch.class);
                startActivity(intent);
            }
        });*/
    }
    private class CurrentLocationListener implements View.OnClickListener{
        @Override
        public void onClick(View view){
            // 検索結果表示画面へ遷移
            String urlStr = "geo:" + latitude + "," + longitude;
            //画面パラメータと一緒に渡す？
            Log.i("ButtonClick","現在地押したー");
            Intent intent = new Intent(MainMenu.this, SearchResultActivity.class);
            startActivity(intent);
        }
    }

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


    //
    private class GPSLocationListener implements LocationListener {

        @Override
        public void onLocationChanged(Location location){
            //引数のLocationオブジェクトから緯度を取得
            latitude = location.getLatitude();
            //引数のLocationオブジェクトから経度を取得
            longitude = location.getLongitude();


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
