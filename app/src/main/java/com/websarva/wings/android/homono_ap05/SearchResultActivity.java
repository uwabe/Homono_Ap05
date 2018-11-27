package com.websarva.wings.android.homono_ap05;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONObject;

import static java.lang.Double.parseDouble;

public class SearchResultActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap googleMap;

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
        // 現在のintentを取得する
        Intent intent = getIntent();
        // intentから指定キーの文字列を取得する
        String lon = intent.getStringExtra( "station_Lon" );
        String lat = intent.getStringExtra( "station_Lat" );

        LatLng pos = new LatLng(parseDouble(lat), parseDouble(lon));
        // ズームにする
        zoomMap(parseDouble(lat), parseDouble(lon));


        // 円を描く
        googleMap.addCircle(new CircleOptions()
                .center(pos)
                .radius(500)
                .strokeColor(Color.RED)
                .fillColor(Color.RED));

        //GoogleMapの情報を手に入れる非同期タスクの生成
        //final googlemaploader googlemaploader = new googlemaploader(this);
        //実行
        //googlemaploader.execute(lat + "," + lon);

    }

    //地図をzoomして表示する
    private void zoomMap(double latitude, double longitude){
        // 表示する東西南北の緯度経度を設定
        double south = latitude * (1-0.00005);
        double west = longitude * (1-0.00005);
        double north = latitude * (1+0.00005);
        double east = longitude * (1+0.00005);

        // LatLngBounds (LatLng southwest, LatLng northeast)
        LatLngBounds bounds = LatLngBounds.builder()
                .include(new LatLng(south , west))
                .include(new LatLng(north, east))
                .build();

        int width = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels;

        // static CameraUpdate.newLatLngBounds(LatLngBounds bounds, int width, int height, int padding)
        googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, width, height, 0));
    }

    //onPostExecuteで実行される関数
    public void result_job(String result){
        setPinHonmono(result);
    }

    public void setPinHonmono(String jsonData) {
        try {
            JSONObject jsonObject = new JSONObject(jsonData);
            JSONArray shopList = jsonObject.getJSONArray("results");

            for (int i = 0; i < shopList.length(); i++) {

                JSONObject jsonObject_shop = shopList.getJSONObject(i);
                JSONObject location = jsonObject_shop.getJSONObject("geometry").getJSONObject("location");

                final String shopName = jsonObject_shop.getString("name");
                final String lat = location.getString("lat");
                final String lon = location.getString("lng");

                MarkerOptions opt = new MarkerOptions();
                //位置情報
                opt.position(new LatLng(parseDouble(lat), parseDouble(lon)));
                //店名
                opt.title(shopName);
                //住所
                opt.snippet(jsonObject_shop.getString("vicinity"));
                Marker marker = googleMap.addMarker(opt);
                //表示する
                marker.showInfoWindow();
                // タップ時のイベントハンドラ登録
                //この辺はまだまだ
                googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker marker) {
                        //SQLLiteでお気に入りに登録する
                        //DatabaseHelper dBHelper = null;
                        try {

                        } catch (Exception e) {
                            Log.d("DB作成時のエラー", e.getMessage());
                        }
                        Toast.makeText(getApplicationContext(), "お気に入りに登録しました", Toast.LENGTH_LONG).show();
                        return false;
                    }
                });
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

        //フッターボタン押下
    public void onMenuButtonClick(View view){
        //メインメニュー画面に戻る
        finish();
    }
}
