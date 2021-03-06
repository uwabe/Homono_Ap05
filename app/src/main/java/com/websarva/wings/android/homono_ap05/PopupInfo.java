package com.websarva.wings.android.homono_ap05;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class PopupInfo extends AppCompatActivity {

    //private SQLiteDatabase db;
    //private DatabaseHelper databaseHelper;
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

        strPlaceId = intent.getStringExtra("placeId");
        strLat = intent.getStringExtra("lat");
        strLon = intent.getStringExtra("lon");
        strOpeningHours = intent.getStringExtra("openingHours");
        Log.i("☆intent確認", "きたのは"+strOpeningHours);
        //Log.i("☆intent確認","onCreate: "+(strOpeningHours.equals("true")));
        TextView txtOpeningHours = findViewById(R.id.txtOpeningNow);
        if (strOpeningHours.equals("true")) {
            txtOpeningHours.setText("現在営業中");
        } else {
            txtOpeningHours.setText("現在営業準備中");
        }
        shopInfoLoader.execute(strPlaceId);
        buttonSelector(strPlaceId);
        //閉じるボタン
        Button backButton = findViewById(R.id.button_back);
        BackListener backListener = new BackListener();
        backButton.setOnClickListener(backListener);
    }

    //onPostExecuteで実行される関数
    public void result_job(String result) {
        setStoreInfo(result);
    }

    public void buttonSelector(String placeId){
        DatabaseHelper databaseHelper = new DatabaseHelper(getApplicationContext());
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        String sqlselect = "select store_name, place_id, lat, lng from StoreTable where place_id ='" + placeId + "'";
        Cursor c = db.rawQuery(sqlselect, null);
        boolean next = c.moveToFirst();
        if (next) {
            //登録済ボタン
            ImageView insertButton=findViewById(R.id.button_insert);
            insertButton.setImageResource(R.drawable.star2);
            InsertListener insertListener = new InsertListener();
            insertButton.setOnClickListener(insertListener);
        } else {
            //登録ボタン
            ImageView insertButton=findViewById(R.id.button_insert);
            insertButton.setImageResource(R.drawable.star1);
            InsertListener insertListener = new InsertListener();
            insertButton.setOnClickListener(insertListener);

        }

    }

    public void setStoreInfo(String jsonData) {
        try {
            Log.i("☆ステータス", "結果" + jsonData);
            JSONObject jsonObject = new JSONObject(jsonData);
            JSONObject jsonObject_info = jsonObject.getJSONObject("result");
            Log.i("☆取得", "結果" + jsonObject_info);
            String address = jsonObject_info.getString("formatted_address");
            address = address.replace("日本、", "");
            String phone_num = jsonObject_info.getString("formatted_phone_number");
            String store_name = jsonObject_info.getString("name");


            Log.i("☆結果:", "住所:" + address + "電話番号:" + phone_num + "店名:" + store_name);
            strShopName = store_name;
            strVicinity = address;
            strPhoneNum = phone_num;
            TextView txtStoreName = findViewById(R.id.txtStoreName);
            txtStoreName.setText(strShopName);
            TextView txtVicinity = findViewById(R.id.txtVicinity);
            txtVicinity.setText(strVicinity);
            Log.i("☆結果:", "住所:" + strShopName);
            TextView txtPhoneNumber = findViewById(R.id.txtPhoneNumber);
            txtPhoneNumber.setText(strPhoneNum);


        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    private class InsertListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {

            DatabaseHelper databaseHelper = new DatabaseHelper(getApplicationContext());
            SQLiteDatabase db = databaseHelper.getWritableDatabase();

            String storeName = strShopName;
            String placeId = strPlaceId;
            String lat = strLat;
            String lng = strLon;
            String sqlselect = "select store_name, place_id, lat, lng from StoreTable where place_id ='" + placeId + "'";
            Log.i("☆値の確認", "確認" + storeName + "," + placeId + "," + lat + "," + lng);
            Cursor c = db.rawQuery(sqlselect, null);
            Log.i("☆確認カーソル", "取れたか:" + c);
            boolean next = c.moveToFirst();
            if (next) {
                Toast.makeText(PopupInfo.this, "お気に入り登録済みです", Toast.LENGTH_LONG).show();
                finish();
            } else {
                insertData(db, storeName, placeId, lat, lng);
                Toast.makeText(PopupInfo.this, "お気に入り登録しました。", Toast.LENGTH_SHORT).show();
                finish();
            }

        }
    }

    private class BackListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            finish();
        }

    }

    private void insertData(SQLiteDatabase db, String storeName, String placeId, String lat, String lng) {

        ContentValues values = new ContentValues();
        values.put("store_name", storeName);
        values.put("place_id", placeId);
        values.put("lat", lat);
        values.put("lng", lng);
        try {
            db.insert("StoreTable", null, values);
        } finally {
            db.close();
        }

    }
}
