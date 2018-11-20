package com.websarva.wings.android.homono_ap05;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainMenu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        //現在地
        Button menubtn1 =findViewById(R.id.button);
        menubtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //画面パラメーターおくる
                //GPS情報渡す
                //Intent intent = new Intent(MainMenu.this, 現在地.class);
                //startActivity(intent);
                Log.i("log1","現在地が押されたー");
            }
        });

        //お気に入り
        Button menubtn2 =findViewById(R.id.button2);
        menubtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("log1","お気に入りが押されたー");
                Intent intent = new Intent(MainMenu.this, FavoriteSearch.class);
                startActivity(intent);
            }
        });

        //駅
        //なーーぜーーかーーエラーーになるーーーはーーーー???Fragmentだからかーーー??????
        Button menubtn3 =findViewById(R.id.button3);
        menubtn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainMenu.this, StationSearch.class);
                startActivity(intent);
            }
        });
    }
}
