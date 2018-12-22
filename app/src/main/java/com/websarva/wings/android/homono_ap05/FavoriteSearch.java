package com.websarva.wings.android.homono_ap05;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_search);
        if(createDataset()==null) {
            //画面に出すのは難しいのでトーストに変更した(2018.11.22MTG　決定)
            Toast.makeText(FavoriteSearch.this, "お気に入りが登録されていません", Toast.LENGTH_SHORT).show();
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
        Cursor c = db.query("StoreTable", new String[]{"store_name", "place_id", "lat", "lng"}, null, null, null, null, null);
        c.moveToFirst();
        List<Map<String, String>> fList = new ArrayList<>();
        Map<String, String> fItem;
        //DB取得結果ぶちこみ
        for (int i = 0; i < c.getCount(); i++) {
            fItem = new HashMap<>();
            fItem.put("store_name", (c.getString(0)));
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
            dataset.add(data);
        }
        return dataset;
    }

    public void MoveSearchResult(String storename){
        //今ここでエラーに
//        if (databaseHelper == null) {
//            databaseHelper = new DatabaseHelper(getApplicationContext());
//        }
//        if (db == null) {
//            db = databaseHelper.getReadableDatabase();
//        }

    }

    //フッターボタン押下
    public void onMenuButtonClick(View view) {
        //メインメニュー画面に戻る
        finish();
    }
}
