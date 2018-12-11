package com.websarva.wings.android.homono_ap05;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

public class PopupInfo extends AppCompatActivity {

    private SQLiteDatabase db;
    private DatabaseHelper databaseHelper;
    private String strShopName;
    private String strVicinity;
    private String strPlaceId;
    private String strPhoneNum;
    private String strOpeningHours;
    private String strLat;
    private String strLon;
    final ShopInfoLoader shopInfoLoader = new ShopInfoLoader(this);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popup_info);
        Intent intent = getIntent();

        //strShopName = intent.getStringExtra("shopName");
        //strVicinity = intent.getStringExtra("vicinity");
        strPlaceId = intent.getStringExtra("placeId");
        //strOpeningHours = intent.getStringExtra("openingHours");
        strLat = intent.getStringExtra("lat");
        strLon = intent.getStringExtra("lon");

        shopInfoLoader.execute(strPlaceId);

        //TextView txtStoreName = findViewById(R.id.txtStoreName);
        //txtStoreName.setText(strShopName);
        //Log.i("☆結果:", "店名:"+strShopName);

        //TextView txtVicinity = findViewById(R.id.txtVicinity);
        //txtVicinity.setText(strVicinity);
        //Log.i("☆結果:", "住所:"+strShopName);

        //TextView txtPhoneNumber = findViewById(R.id.txtPhoneNumber);
        //txtPhoneNumber.setText(strPhoneNum);

        //TextView txtOpeningHours = findViewById(R.id.txtOpeningNow);
        //txtOpeningHours.setText(strOpeningHours);
//        if (strOpeningHours == "true"){
////            txtOpeningHours.setText("営業中");
////        }else {
////            txtOpeningHours.setText("営業時間外");
////        }
        //登録ボタン
        Button insertButton = findViewById(R.id.button_insert);
        InsertListener insertListener = new InsertListener();
        insertButton.setOnClickListener(insertListener);
        //閉じるボタン
        Button backButton = findViewById(R.id.button_back);
        BackListener backListener = new BackListener();
        backButton.setOnClickListener(backListener);
    }

    //onPostExecuteで実行される関数
    public void result_job(String result) {
        setStoreInfo(result);
    }

    public void setStoreInfo(String jsonData){
        try{
            Log.i("☆ステータス", "結果"+jsonData);
            JSONObject jsonObject = new JSONObject(jsonData);
            //JSONArray shopInfoList = jsonObject.getJSONArray("results");
            JSONObject jsonObject_info = jsonObject.getJSONObject("result");
            Log.i("☆取得", "結果"+jsonObject_info);
            String address = jsonObject_info.getString("formatted_address");
            String phone_num = jsonObject_info.getString("formatted_phone_number");
            String store_name = jsonObject_info.getString("name");
            //String weeklist =jsonObject_info.getString("weekday_text");

            Log.i("☆結果:", "住所:"+address+"電話番号:"+phone_num+"店名:"+store_name);
            strShopName = store_name;
            strVicinity = address;
            strPhoneNum = phone_num;
            //strOpeningHours = weeklist;
            TextView txtStoreName = findViewById(R.id.txtStoreName);
            txtStoreName.setText(strShopName);
            TextView txtVicinity = findViewById(R.id.txtVicinity);
            txtVicinity.setText(strVicinity);
            Log.i("☆結果:", "住所:"+strShopName);
            TextView txtPhoneNumber = findViewById(R.id.txtPhoneNumber);
            txtPhoneNumber.setText(strPhoneNum);




        }catch (Exception ex){
            ex.printStackTrace();
        }

    }

    private class InsertListener implements View.OnClickListener{
        @Override
        public void onClick(View view){

            if (databaseHelper == null){
                databaseHelper = new DatabaseHelper(getApplicationContext());
            }

            if (db == null){
                db = databaseHelper.getWritableDatabase();
            }

            String storeName = strShopName;
            String placeId = strPlaceId;
            String lat = strLat;
            String lng = strLon;

            insertData(db,storeName,placeId,lat,lng);

//            String key = editTextKey.getText().toString();
//            String value = editTextValue.getText().toString();
//
//            insertData(db,key,value);

            finish();

        }
    }

    private class BackListener implements View.OnClickListener {
        @Override
        public void onClick(View view){
            finish();
        }

    }
    private void insertData(SQLiteDatabase db,String storeName,String placeId,String lat,String lng){

        ContentValues values = new ContentValues();
        values.put("store_name", storeName);
        values.put("place_id",placeId);
        values.put("lat", lat);
        values.put("lng", lng);
//        values.put("address", address);

        db.insert("storeTable",null,values);
//        db.insert("sampleTable",null,values);

    }
}
