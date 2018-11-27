package com.websarva.wings.android.homono_ap05;

import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
    private ListView listView;
    private SQLiteDatabase db;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_search);
        Intent intent = getIntent();
        //Listviewに中身を入れる
        listView = findViewById(R.id.favorite_list);
        readData();
        /*SQLiteDatabase db = databaseHelper.getWritableDatabase();
        Cursor c = db.rawQuery("select * from HMG001", null);
        //selectがnullなら文字をだして終了
        //selectいくつか件数あった場合下の
        String[] from = {"store_name","_id"};
        int[] to = {android.R.id.text1,android.R.id.text2};
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_2,c,from,to,0);
        favoriteList.setAdapter(adapter);
        favoriteList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String s1 = ((TextView)view.findViewById(android.R.id.text1)).getText().toString();
                String s2 = ((TextView)view.findViewById(android.R.id.text2)).getText().toString();
                Log.v("test1","position=" + s1);
                Log.v("test2","position=" + s2);
            }
        });*/
    }

    private void readData() {
        //空だったら作成・読み込み
        if (databaseHelper == null) {
            databaseHelper = new DatabaseHelper(getApplicationContext());
        }
        if (db == null) {
            db = databaseHelper.getReadableDatabase();
        }

        //DBの中身確認(select all)
        long selectCount = DatabaseUtils.queryNumEntries(db, "HMG001");

        //0件の場合
        if (selectCount == 0) {
            //画面に出すのは難しいのでトーストに変更した(2018.11.22MTG　決定)
            Toast.makeText(FavoriteSearch.this, "お気に入りが登録されていません", Toast.LENGTH_SHORT).show();
            return;
        }

        //1件以上の場合
        //nullの種類
        //1. selection
        //2. selectionArgs
        //3. group by
        //4. having
        //5. order by
        Cursor c = db.query("HMG001", new String[]{"store_name", "place_id", "lat", "lng"}, null, null, null, null, null);
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

        c.close();

        //さらに細かく
        String[] items;
        int count = fList.size();
        items = new String[count];

        for (int i = 0; i < count; i++) {
            items[i] = fList.get(i).get("store_name");
        }

        //画面表示
        //ListAdapter listadapter = new ListAdapter(FavoriteSearch.this,R.layout.row,items);
        //listView.setAdapter(listadapter);
        //listView.setOnItemClickListener(new ListItemClickListener());
    }


      private class ListItemClickListener implements AdapterView.OnItemClickListener{

      @Override public void onItemClick(AdapterView<?> parent, View view, int position,long id){
          switch (view.getId()){
              case R.id.row_text:

                  break;
              //case R.id.row_button:
                  //Log.d(TAG, String.valueOf(position) + "件目の削除ボタン");
                  //break;
          }
      }
      }

    //フッターボタン押下
    public void onMenuButtonClick(View view) {
        //メインメニュー画面に戻る
        finish();
    }
}
