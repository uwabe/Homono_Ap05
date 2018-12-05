package com.websarva.wings.android.homono_ap05;

import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.LocationSource;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;


import org.json.JSONArray;
import org.json.JSONObject;

import static java.lang.Double.parseDouble;

public class SearchResultActivity extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        LocationListener, GoogleMap.OnMyLocationButtonClickListener, LocationSource {

    GoogleMap googleMap;
    private GoogleApiClient mGoogleApiClient;
    LocationRequest locationRequest;
    CameraUpdate cameraUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);
        SupportMapFragment supportmapfragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        supportmapfragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMap = googleMap;
        //Googleが提供しているサービスに対して接続するためのクライアント
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        mGoogleApiClient.connect();


        // ズームにする
        //zoomMap(lat_d, lon_d);

        // 円を描く
//        googleMap.addCircle(new CircleOptions()
//                .center(pos)
//                .radius(500)
//                .strokeColor(Color.RED)
//                .fillColor(Color.RED));

        //GoogleMapの情報を手に入れる非同期タスクの生成
        //final GoogleDataLoader googleDataLoader = new GoogleDataLoader(this);
        //実行
        //googleDataLoader.execute(lat + "," + lon);
        // 現在のintentを取得する
        Intent intent = getIntent();
        // intentから指定キーの文字列を取得する
        String lon = intent.getStringExtra("result_lon");
        String lat = intent.getStringExtra("result_lat");
        //画面から渡されるパラメータ 1:現在地、2:お気に入り、3:駅名
        int pageParam = intent.getIntExtra("page_param", 0);
        //駅名はplace_idを受け取る？
        String place_id = intent.getStringExtra("place_id");
        Log.i("Search1", "現在地:" + lon + "&" + lat);
        Double lon_d = parseDouble(lon);
        Double lat_d = parseDouble(lat);
        Log.i("Search2", "現在地:" + lon_d + "&" + lat_d);

        LatLng pos = new LatLng(lat_d, lon_d);
        //マーカーに関する記述は後で
        //googleMap.addMarker(new MarkerOptions().position(pos).title("first_pin"));
        //中心地は各画面から渡された経度と緯度
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(pos));
        cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(lat_d, lon_d), 15);
        googleMap.moveCamera(cameraUpdate);

        //分岐
        switch (pageParam) {
            //現在地の場合
            case 1:
                Log.i("Bunki", "現在地から来た");
                // 円を描く
                googleMap.addCircle(new CircleOptions()
                        .center(pos)
                        .radius(500)
                        .strokeColor(Color.BLUE)
                        .fillColor(Color.LTGRAY));
                break;
            //お気に入りの場合
            case 2:
                Log.i("Bunki", "お気に入りから来た");
                break;
            //駅名の場合
            case 3:
                Log.i("Bunki", "駅から来た");
                // 円を描く
                googleMap.addCircle(new CircleOptions()
                        .center(pos)
                        .radius(500)
                        .strokeColor(Color.BLUE)
                        //ここ薄いグレーにできるの・・・?
                        .fillColor(Color.LTGRAY));
                break;
        }
    }

//    //地図をzoomして表示する
//    private void zoomMap(GoogleMap gMap,double latitude, double longitude) {
//        // 表示する東西南北の緯度経度を設定
//        double south = latitude * (1 - 0.00005);
//        double west = longitude * (1 - 0.00005);
//        double north = latitude * (1 + 0.00005);
//        double east = longitude * (1 + 0.00005);
//
//        Log.i("result3","南:"+ south +"西:"+west+"北:"+north+"東:"+east);
//        LatLngBounds bounds = LatLngBounds.builder()
//                .include(new LatLng(south, west))
//                .include(new LatLng(north, east))
//                .build();
//        int width = getResources().getDisplayMetrics().widthPixels;
//        int height = getResources().getDisplayMetrics().heightPixels;
//        Log.i("result3","幅:"+ width +"高さ:"+height);
//        //Log.i("result3", String.valueOf(googleMap));
//        // static CameraUpdate.newLatLngBounds(LatLngBounds bounds, int width, int height, int padding)
//        gMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, width, height, 0));
//    }

    //onPostExecuteで実行される関数
    public void result_job(String result) {
        //setPinHonmono(result);
    }


//    public void setPinHonmono(String jsonData) {
//        try {
//            JSONObject jsonObject = new JSONObject(jsonData);
//            JSONArray shopList = jsonObject.getJSONArray("results");
//
//            for (int i = 0; i < shopList.length(); i++) {
//
//                JSONObject jsonObject_shop = shopList.getJSONObject(i);
//                JSONObject location = jsonObject_shop.getJSONObject("geometry").getJSONObject("location");
//
//                final String shopName = jsonObject_shop.getString("name");
//                final String lat = location.getString("lat");
//                final String lon = location.getString("lng");
//
//                MarkerOptions opt = new MarkerOptions();
//                //位置情報
//                opt.position(new LatLng(parseDouble(lat), parseDouble(lon)));
//                //店名
//                opt.title(shopName);
//                //住所
//                opt.snippet(jsonObject_shop.getString("vicinity"));
//                Marker marker = googleMap.addMarker(opt);
//                //表示する
//                marker.showInfoWindow();
//                // タップ時のイベントハンドラ登録
//                //この辺はまだまだ
//                googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
//                    @Override
//                    public boolean onMarkerClick(Marker marker) {
//                        //SQLLiteでお気に入りに登録する
//                        //DatabaseHelper dBHelper = null;
//                        try {
//
//                        } catch (Exception e) {
//                            Log.d("DB作成時のエラー", e.getMessage());
//                        }
//                        Toast.makeText(getApplicationContext(), "お気に入りに登録しました", Toast.LENGTH_LONG).show();
//                        return false;
//                    }
//                });
//            }
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//    }

    //フッターボタン押下
    public void onMenuButtonClick(View view) {
        //メインメニュー画面に戻る
        finish();
    }

    @Override
    public void onLocationChanged(Location location) {
//        if(location == null){
//            Toast.makeText(getApplicationContext(),"Location not found",Toast.LENGTH_SHORT).show();
//        }else{
//            LatLng latLngCurrent = new LatLng(location.getLatitude(),location.getLongitude());
//        }

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        //この辺非推奨なんですけど
        //locationRequest = new LocationRequest().create();
        //locationRequest.setInterval(1000);
        //locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        //LocationServices.FusedLocationApi.requestLocationUpdates(
        //        mGoogleApiClient, locationRequest, this);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public boolean onMyLocationButtonClick() {
        return false;
    }

    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {

    }

    @Override
    public void deactivate() {

    }
}