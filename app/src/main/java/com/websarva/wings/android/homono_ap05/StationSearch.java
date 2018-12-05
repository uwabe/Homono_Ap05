package com.websarva.wings.android.homono_ap05;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class StationSearch extends AppCompatActivity implements View.OnClickListener {
    private ArrayList<Prefectures> prefecturesArrayList = new ArrayList<>();
    private ArrayList<Lines> linesArrayList = new ArrayList<>();
    private ArrayList<Stations> stationsArrayList = new ArrayList<>();

    private String station_lon = "";
    private String station_lat = "";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_station_search);

        //都道府県のスピナー設定
        Spinner prefSpinner = findViewById(R.id.PrefSpinner);
        setPrefData();
        ArrayAdapter<Prefectures> adapter_pref = new ArrayAdapter<Prefectures>(this, android.R.layout.simple_spinner_dropdown_item, prefecturesArrayList);
        adapter_pref.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        prefSpinner.setAdapter(adapter_pref);

        //スピナーが選択されたら
        prefSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Prefectures prefectures = (Prefectures) adapterView.getSelectedItem();
                String prefId = prefectures.getId();
                String prefName = prefectures.getName();
                ekidataloader_job("都道府県", prefId);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void ekidataloader_job(String spinner_name, String selected_id) {
        final EkidataLoader ekidataLoader = new EkidataLoader(this);
        ekidataLoader.execute(spinner_name, selected_id);
    }

    public void result_job(String spinner_name, String result) {
        if (spinner_name == "都道府県") {
            setLineData(result);
        } else if (spinner_name == "路線") {
            setStationData(result);
        }
    }

    //路線スピナーの設定
        private void setLineData(String lineData){
            try{
                //接続結果のjsonをぶっこむ
                JSONObject jsonObject = new JSONObject(lineData);
                JSONArray jsonArray_line = jsonObject.getJSONArray("line");
                Spinner lineSpinner = findViewById(R.id.LineSpinner);
                ArrayAdapter<Lines> adapter_line = new ArrayAdapter<Lines>(this, android.R.layout.simple_spinner_dropdown_item,linesArrayList);
                adapter_line.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                adapter_line.clear();
                for (int i=0; i<jsonArray_line.length();i++){
                    JSONObject item_line = jsonArray_line.getJSONObject(i);
                    linesArrayList.add(new Lines(item_line.getString("line_cd"),item_line.getString("line_name")));
                }
                lineSpinner.setAdapter(adapter_line);

                //スピナーが選択されたら
                lineSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        Lines lines = (Lines) adapterView.getSelectedItem();
                        String lineId = lines.getId();
                        String lineName = lines.getName();
                        ekidataloader_job("路線", lineId);
                        Log.e("data","IDは"+lineId);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });
            }catch (Exception e){
                e.printStackTrace();
            }
    }

    //駅スピナー
    private void setStationData(String staData) {
        try {
            //接続結果のjsonをぶっこむ
            JSONObject jsonObject = new JSONObject(staData);
            JSONArray jsonArray_sta = jsonObject.getJSONArray("station_l");
            Spinner staSpinner = findViewById(R.id.StaSpinner);
            ArrayAdapter<Stations> adapter_sta = new ArrayAdapter<Stations>(this, android.R.layout.simple_spinner_dropdown_item, stationsArrayList);
            adapter_sta.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            adapter_sta.clear();
            for (int i = 0; i < jsonArray_sta.length(); i++) {
                JSONObject item_sta = jsonArray_sta.getJSONObject(i);
                stationsArrayList.add(new Stations(item_sta.getString("station_cd"), item_sta.getString("station_name"),item_sta.getString("lon"),item_sta.getString("lat")));
            }
            staSpinner.setAdapter(adapter_sta);

            //スピナーが選択されたら
            staSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    Stations stations = (Stations) adapterView.getSelectedItem();
                    station_lon = stations.getLon();
                    station_lat = stations.getLat();
                    Log.i("result1","lon:"+ station_lon+" lat:"+ station_lat);
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onClick(View view) {
        Spinner prefSpinner = findViewById(R.id.PrefSpinner);
        if (prefSpinner.getSelectedItemPosition()==0){
            Toast.makeText(this, "駅が選択されていません", Toast.LENGTH_SHORT).show();
        }else{
        Intent intent = new Intent(this,SearchResultActivity.class);
        Log.i("StaButtonClick","現在地:"+ station_lon+ "&" + station_lat);
        String lon = String.valueOf(station_lon);
        String lat = String.valueOf(station_lat);
        intent.putExtra("result_lon",lon);
        intent.putExtra("result_lat",lat);
        Log.i("StaButtonClick1","駅押したー");
        Log.i("StaButtonClick2","現在地:"+ lon+ "&" + lat);
        intent.putExtra("page_param",3);
        startActivity(intent);
        }
    }

    private void setPrefData(){
        prefecturesArrayList.add(new Prefectures("0", "-----"));
        prefecturesArrayList.add(new Prefectures("1", "北海道"));
        prefecturesArrayList.add(new Prefectures("2", "青森県"));
        prefecturesArrayList.add(new Prefectures("3", "岩手県"));
        prefecturesArrayList.add(new Prefectures("4", "宮城県"));
        prefecturesArrayList.add(new Prefectures("5", "秋田県"));
        prefecturesArrayList.add(new Prefectures("6", "山形県"));
        prefecturesArrayList.add(new Prefectures("7", "福島県"));
        prefecturesArrayList.add(new Prefectures("8", "茨城県"));
        prefecturesArrayList.add(new Prefectures("9", "栃木県"));
        prefecturesArrayList.add(new Prefectures("10", "群馬県"));
        prefecturesArrayList.add(new Prefectures("11", "埼玉県"));
        prefecturesArrayList.add(new Prefectures("12", "千葉県"));
        prefecturesArrayList.add(new Prefectures("13", "東京都"));
        prefecturesArrayList.add(new Prefectures("14", "神奈川県"));
        prefecturesArrayList.add(new Prefectures("15", "新潟県"));
        prefecturesArrayList.add(new Prefectures("16", "富山県"));
        prefecturesArrayList.add(new Prefectures("17", "石川県"));
        prefecturesArrayList.add(new Prefectures("18", "福井県"));
        prefecturesArrayList.add(new Prefectures("19", "山梨県"));
        prefecturesArrayList.add(new Prefectures("20", "長野県"));
        prefecturesArrayList.add(new Prefectures("21", "岐阜県"));
        prefecturesArrayList.add(new Prefectures("22", "静岡県"));
        prefecturesArrayList.add(new Prefectures("23", "愛知県"));
        prefecturesArrayList.add(new Prefectures("24", "三重県"));
        prefecturesArrayList.add(new Prefectures("25", "滋賀県"));
        prefecturesArrayList.add(new Prefectures("26", "京都府"));
        prefecturesArrayList.add(new Prefectures("27", "大阪府"));
        prefecturesArrayList.add(new Prefectures("28", "兵庫県"));
        prefecturesArrayList.add(new Prefectures("29", "奈良県"));
        prefecturesArrayList.add(new Prefectures("30", "和歌山県"));
        prefecturesArrayList.add(new Prefectures("31", "鳥取県"));
        prefecturesArrayList.add(new Prefectures("32", "島根県"));
        prefecturesArrayList.add(new Prefectures("33", "岡山県"));
        prefecturesArrayList.add(new Prefectures("34", "広島県"));
        prefecturesArrayList.add(new Prefectures("35", "山口県"));
        prefecturesArrayList.add(new Prefectures("36", "徳島県"));
        prefecturesArrayList.add(new Prefectures("37", "香川県"));
        prefecturesArrayList.add(new Prefectures("38", "愛媛県"));
        prefecturesArrayList.add(new Prefectures("39", "高知県"));
        prefecturesArrayList.add(new Prefectures("40", "福岡県"));
        prefecturesArrayList.add(new Prefectures("41", "佐賀県"));
        prefecturesArrayList.add(new Prefectures("42", "長崎県"));
        prefecturesArrayList.add(new Prefectures("43", "熊本県"));
        prefecturesArrayList.add(new Prefectures("44", "大分県"));
        prefecturesArrayList.add(new Prefectures("45", "宮崎県"));
        prefecturesArrayList.add(new Prefectures("46", "鹿児島県"));
        prefecturesArrayList.add(new Prefectures("47", "沖縄県"));
    }
    //フッターボタン押下
    public void onMenuButtonClick(View view){
        //メインメニュー画面に戻る
        finish();
    }
}
