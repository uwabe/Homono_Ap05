package com.websarva.wings.android.homono_ap05;

import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
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

    private GoogleMap googleMap;
    private String lon_intent;
    private String lat_intent;
    private String place_id_intent;
    private String open_now_intent;
    private GoogleApiClient mGoogleApiClient;
    LocationRequest locationRequest;
    CameraUpdate cameraUpdate;
    final GoogleDataLoader googleDataLoader = new GoogleDataLoader(this);
    private int pageParam;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);
        SupportMapFragment supportmapfragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        supportmapfragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap gMap) {
        googleMap = gMap;
        //Googleが提供しているサービスに対して接続するためのクライアント
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        mGoogleApiClient.connect();


        // 現在のintentを取得する
        Intent intent = getIntent();
        // intentから指定キーの文字列を取得する
        lon_intent = intent.getStringExtra("result_lon");
        lat_intent = intent.getStringExtra("result_lat");
        //画面から渡されるパラメータ 1:現在地、2:お気に入り、3:駅名
        pageParam = intent.getIntExtra("page_param", 0);
        //駅名はplace_idを受け取る？
        place_id_intent = intent.getStringExtra("place_id");
        Log.i("Search1", "現在地:" + lon_intent + "&" + lat_intent);
        Double lon_d = parseDouble(lon_intent);
        Double lat_d = parseDouble(lat_intent);
        Log.i("Search2", "現在地:" + lon_d + "&" + lat_d);

        LatLng pos = new LatLng(lat_d, lon_d);
        String urlDraft = "";
        //分岐
        switch (pageParam) {
            //現在地の場合
            case 1:
                Log.i("Bunki", "現在地から来た");
                // ズームにする
                zoomMap(googleMap, lat_d, lon_d);
                urlDraft = lat_intent + "," + lon_intent;
                //実行
                googleDataLoader.execute(urlDraft);
                // 円を描く
                googleMap.addCircle(new CircleOptions()
                        .center(pos)
                        .radius(500)
                        .strokeColor(Color.RED)
                        .fillColor(Color.argb(150, 204, 204, 204)));
                break;
            //お気に入りの場合
            case 2:
                Log.i("Bunki", "お気に入りから来た");
                // ズームにする
                zoomMap(googleMap, lat_d, lon_d);
                urlDraft = lat_intent + "," + lon_intent;
                //実行
                googleDataLoader.execute(urlDraft);
                break;
            //駅名の場合
            case 3:
                Log.i("Bunki", "駅から来た");
                // ズームにする
                zoomMap(googleMap, lat_d, lon_d);
                urlDraft = lat_intent + "," + lon_intent;
                //実行
                googleDataLoader.execute(urlDraft);

                // 円を描く
                googleMap.addCircle(new CircleOptions()
                        .center(pos)
                        .radius(500)
                        .strokeColor(Color.RED)
                        .fillColor(Color.argb(150, 204, 204, 204)));
                break;
        }
    }

    //地図をzoomして表示する
    private void zoomMap(GoogleMap gMap, double latitude, double longitude) {
        // 表示する東西南北の緯度経度を設定
        double south = latitude * (1 - 0.00005);
        double west = longitude * (1 - 0.00005);
        double north = latitude * (1 + 0.00005);
        double east = longitude * (1 + 0.00005);

        Log.i("result3", "南:" + south + "西:" + west + "北:" + north + "東:" + east);
        LatLngBounds bounds = LatLngBounds.builder()
                .include(new LatLng(south, west))
                .include(new LatLng(north, east))
                .build();
        int width = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels;
        Log.i("result3", "幅:" + width + "高さ:" + height);
        // static CameraUpdate.newLatLngBounds(LatLngBounds bounds, int width, int height, int padding)
        gMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, width, height, 0));
    }

    //onPostExecuteで実行される関数
    public void result_job(String result) {
        setPinHonmono(result);
    }


    public void setPinHonmono(String jsonData) {
        String placeId;
        String lat;
        String lon;
        String openNow;
        final String[] placeidlist = new String[4];
        final String[] latlist = new String[4];
        final String[] lonlist = new String[4];
        final String[] openNowlist = new String[4];
        try {
            boolean iszero = jsonData.contains("ZERO_RESULTS");
            Log.i("☆ステータス", "結果:" + iszero);
            if (jsonData.contains("ZERO_RESULTS")) {
                Toast.makeText(SearchResultActivity.this, "店舗がありません", Toast.LENGTH_LONG).show();

            } else {
                Log.i("☆ステータス", "結果" + jsonData);
                JSONObject jsonObject = new JSONObject(jsonData);
                JSONArray shopList = jsonObject.getJSONArray("results");
                Log.i("☆遷移確認", "どこから:" +pageParam);
                if (pageParam == 2){
                    JSONObject jsonObject_shop = shopList.getJSONObject(0);
                    JSONObject openingHours = jsonObject_shop.getJSONObject("opening_hours");
                    open_now_intent = openingHours.getString("open_now");
                    MarkerOptions opt = new MarkerOptions();
                    opt.position(new LatLng(parseDouble(lat_intent), parseDouble(lon_intent)));
                    Marker marker = googleMap.addMarker(opt);
                    googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener(){
                        @Override
                        public boolean onMarkerClick(Marker marker) {
                            Intent intent = new Intent(SearchResultActivity.this, PopupInfo.class);
                            intent.putExtra("placeId", place_id_intent);
                            intent.putExtra("lat", lat_intent);
                            intent.putExtra("lon", lon_intent);
                            intent.putExtra("openingHours",open_now_intent);
                            startActivity(intent);
                            return false;
                        }//click
                    });//clicklistener
                    Log.i("☆あたいは", "place:"+place_id_intent+","+lat_intent+","+lon_intent+","+open_now_intent);
                Intent it = new Intent(SearchResultActivity.this, PopupInfo.class);
                    it.putExtra("placeId", place_id_intent);
                    it.putExtra("lat", lat_intent);
                    it.putExtra("lon", lon_intent);
                    it.putExtra("openingHours",open_now_intent);
                    startActivity(it);

                    return;
                }//if
                for (int i = 0; i < shopList.length(); i++) {
                    if(i<5) {
                        JSONObject jsonObject_shop = shopList.getJSONObject(i);
                        JSONObject location = jsonObject_shop.getJSONObject("geometry").getJSONObject("location");
                        JSONObject openingHours = jsonObject_shop.getJSONObject("opening_hours");
                        placeId = jsonObject_shop.getString("place_id");//店舗ID
                        lat= location.getString("lat");
                        lon= location.getString("lng");
                        openNow = openingHours.getString("open_now");
                        Log.i("☆ID", placeId);
                        Log.i("☆緯度経度", lat + "," + lon);
                        placeidlist[i] = placeId;
                        latlist[i] = lat;
                        lonlist[i] = lon;
                        openNowlist[i]=openNow;
                        MarkerOptions opt = new MarkerOptions();
                        //位置情報
                        opt.position(new LatLng(parseDouble(lat), parseDouble(lon)));

                        //Log.i("☆配列確認2", placeidlist[0]+","+latlist[0]+","+lonlist[0]);
                        Marker marker = googleMap.addMarker(opt);
                        //表示する
                        //marker.showInfoWindow();
                        // タップ時のイベントハンドラ登録
                        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                            @Override
                            public boolean onMarkerClick(Marker marker) {
                                String makerid = marker.getId();
                                Log.i("☆マーカー", "試し" + makerid);
                                if (makerid.equals("m0")) {
                                    Intent intent = new Intent(SearchResultActivity.this, PopupInfo.class);
                                    intent.putExtra("placeId", placeidlist[0]);
                                    intent.putExtra("lat", latlist[0]);
                                    intent.putExtra("lon", lonlist[0]);
                                    intent.putExtra("openingHours",openNowlist[0]);
                                    startActivity(intent);
                                } else if (makerid.equals("m1")) {
                                    Intent intent = new Intent(SearchResultActivity.this, PopupInfo.class);
                                    intent.putExtra("placeId", placeidlist[1]);
                                    intent.putExtra("lat", latlist[1]);
                                    intent.putExtra("lon", lonlist[1]);
                                    intent.putExtra("openingHours",openNowlist[1]);
                                    startActivity(intent);
                                } else if (makerid.equals("m2")) {
                                    Intent intent = new Intent(SearchResultActivity.this, PopupInfo.class);
                                    intent.putExtra("placeId", placeidlist[2]);
                                    intent.putExtra("lat", latlist[2]);
                                    intent.putExtra("lon", lonlist[2]);
                                    intent.putExtra("openingHours",openNowlist[2]);
                                    startActivity(intent);
                                } else if (makerid.equals("m3")) {
                                    Intent intent = new Intent(SearchResultActivity.this, PopupInfo.class);
                                    intent.putExtra("placeId", placeidlist[3]);
                                    intent.putExtra("lat", latlist[3]);
                                    intent.putExtra("lon", lonlist[3]);
                                    intent.putExtra("openingHours",openNowlist[3]);
                                    startActivity(intent);
                                } else if (makerid.equals("m4")) {
                                    Intent intent = new Intent(SearchResultActivity.this, PopupInfo.class);
                                    intent.putExtra("placeId", placeidlist[4]);
                                    intent.putExtra("lat", latlist[4]);
                                    intent.putExtra("lon", lonlist[4]);
                                    intent.putExtra("openingHours",openNowlist[4]);
                                    startActivity(intent);
                                }
                                return false;
                            }//marker click
                        });//listener
                    }
                }//for文
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    //フッターボタン押下
    public void onMenuButtonClick(View view) {
        Intent intent = new Intent(getApplication(), MainMenu.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        //メインメニュー画面に戻る
        //finish();
    }

    @Override
    public void onLocationChanged(Location location) {


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
