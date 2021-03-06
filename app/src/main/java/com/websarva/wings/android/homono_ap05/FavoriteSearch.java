package com.websarva.wings.android.homono_ap05;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Canvas;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FavoriteSearch extends AppCompatActivity {

    //int position_id = 0;
    //String store_name ="";
    private SQLiteDatabase db;
    private DatabaseHelper databaseHelper;

    //public FavoriteSearch(){}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_search);
        if(createDataset()==null) {
            //画面に出すのは難しいのでトーストに変更した(2018.11.22MTG　決定)
            Toast.makeText(FavoriteSearch.this, "お気に入り登録されている店舗情報がありません。", Toast.LENGTH_SHORT).show();
        }else{
            final RecyclerView recyclerView = findViewById(R.id.FavoriteRecyclerView);
            final FavoriteRecycleViewAdapter adapter = new FavoriteRecycleViewAdapter(this, createDataset());
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(linearLayoutManager);
            recyclerView.setAdapter(adapter);
            adapter.setOnItemClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    //Toast.makeText(FavoriteSearch.this, "サンプル:",Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    // init swipe to dismiss logic
//    ItemTouchHelper swipeToDismissTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(
//            ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
//        @Override
//        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
//            return false;
//        }
//
//        @Override
//        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
//           List<RowData> dataset = new ArrayList<>();
//           ;
//            Log.i("☆ポジションは", "値は:"+viewHolder.getAdapterPosition());
//           dataset.remove(viewHolder.getAdapterPosition());
//            databaseHelper = new DatabaseHelper(getApplicationContext());
//            db = databaseHelper.getWritableDatabase();
//            //db.execSQL("delete from FOOD_TABLE where price = (select MIN(price) from FOOD_TABLE)");
//            String listname= dataset.get(direction).getFavoriteTitle();
//            Log.i("☆確認", "onSwiped: "+listname);
//            String sqlStr = "delete from StoreTable where store_name = '"+ listname+"'";
//            db.execSQL(sqlStr,null);
//
//            final FavoriteRecycleViewAdapter adapter =new FavoriteRecycleViewAdapter(FavoriteSearch.this,createDataset());
//            adapter.notifyItemRemoved(viewHolder.getAdapterPosition());
//        }
//
//        @Override
//        public void onChildDrawOver(Canvas c, RecyclerView recyclerView,
//                                    RecyclerView.ViewHolder viewHolder, float dX, float dY,
//                                    int actionState, boolean isCurrentlyActive) {
//        }
//    });


    private List<RowData> createDataset(){
        if (databaseHelper == null) {
            databaseHelper = new DatabaseHelper(getApplicationContext());
        }
        if (db == null) {
            db = databaseHelper.getReadableDatabase();
        }
        //DBの中身確認(select all)
        long selectCount = DatabaseUtils.queryNumEntries(db, "StoreTable");
        //0件の場合
        if (selectCount == 0) {


            return null;
        }
        //1件以上の場合
        Cursor c = db.query("StoreTable", new String[]{"store_name", "place_id", "lat", "lng"}, null, null, null, null, "_id"+" DESC");
        c.moveToFirst();
        List<Map<String, String>> fList = new ArrayList<>();
        Map<String, String> fItem;
        //DB取得結果ぶちこみ
        for (int i = 0; i < c.getCount(); i++) {
            fItem = new HashMap<>();
            fItem.put("store_name", (c.getString(0)));
            fItem.put("place_id",(c.getString(1)));
            fItem.put("lat",(c.getString(2)));
            fItem.put("lng",(c.getString(3)));
            fList.add(fItem);
            c.moveToNext();
        }
        c.close();//カーソル閉じる

        //さらに細かく
        List<RowData> dataset = new ArrayList<>();
        int count = fList.size();
        for (int i = 0; i < count; i++) {
            RowData data = new RowData();
            data.setFavoriteTitle(fList.get(i).get("store_name"));
            data.setPlaceid(fList.get(i).get("place_id"));
            data.setLat(fList.get(i).get("lat"));
            data.setLng(fList.get(i).get("lng"));
            dataset.add(data);
        }
        return dataset;
    }

    public void MoveSearchResult(String placeid, String lat, String lng){
        Log.i("☆インテント確認", "値:"+placeid+","+lat+","+lng);
        Intent intent = new Intent(getApplicationContext(), SearchResultActivity.class);
        intent.putExtra("place_id", placeid);
        intent.putExtra("result_lat", lat);
        intent.putExtra("result_lon", lng);
        intent.putExtra("page_param",2);
        startActivity(intent);
    }

    //フッターボタン押下
    public void onMenuButtonClick(View view) {
        Intent intent = new Intent(getApplication(), MainMenu.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        //メインメニュー画面に戻る
        finish();
    }
}
