package com.websarva.wings.android.homono_ap05;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class PopupInfo extends AppCompatActivity {

    private SQLiteDatabase db;
    private DatabaseHelper databaseHelper;
    private String strShopName;
    private String strVicinity;
    private String strPlaceId;
    private String strOpeningHours;
    private String strLat;
    private String strLon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popup_info);

        Intent intent = getIntent();

        strShopName = intent.getStringExtra("shopName");
        strVicinity = intent.getStringExtra("vicinity");
        strPlaceId = intent.getStringExtra("placeId");
        strOpeningHours = intent.getStringExtra("openingHours");
        strLat = intent.getStringExtra("lat");
        strLon = intent.getStringExtra("lon");

        TextView txtStoreName = findViewById(R.id.txtStoreName);
        txtStoreName.setText(strShopName);

        TextView txtVicinity = findViewById(R.id.txtVicinity);
        txtVicinity.setText(strVicinity);

        TextView txtOpeningHours = findViewById(R.id.txtOpeningNow);
        if (strOpeningHours == "true"){
            txtOpeningHours.setText("営業中");
        }else {
            txtOpeningHours.setText("営業時間外");
        }

        Button insertButton = findViewById(R.id.button_insert);
        InsertListener listener = new InsertListener();
        insertButton.setOnClickListener(listener);
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
