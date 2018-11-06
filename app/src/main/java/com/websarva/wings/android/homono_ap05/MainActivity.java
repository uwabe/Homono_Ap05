package com.websarva.wings.android.homono_ap05;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends FragmentActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener, LoaderManager.LoaderCallbacks<ArrayList<HashMap<String, String>>>{

    private final MainActivity self = this;

    private Spinner prefSpinner;
    private Spinner lineSpinner;
    private Spinner staSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        prefSpinner = findViewById(R.id.PrefSpinner);
        lineSpinner = findViewById(R.id.LineSpinner);
        staSpinner = findViewById(R.id.StaSpinner);

        prefSpinner.setOnItemSelectedListener(self);
        lineSpinner.setOnItemSelectedListener(self);
        staSpinner.setOnItemSelectedListener(self);

        findViewById(R.id.SearchStartButton).setOnClickListener(self);
        getSupportFragmentManager();
    }

    @Override
    public void onClick(View view) {
        if (staSpinner.getSelectedItemPosition() == 0) {
            // 駅が選択されていない
            Toast.makeText(self, "駅が選択されていません", Toast.LENGTH_SHORT).show();
            return;
        }
        //緯度経度をげっとしてページ遷移する
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        if (i == 0) {
            // アイテムが選択されていない
            return;
        }
        // アイテムを取得してリクエストにセット
        HashMap<String, String> item = (HashMap<String, String>) adapterView.getAdapter().getItem(i);
        String sentCode = "1";
        Bundle bundle = new Bundle();
        //bundle.putString("code", item.get("code"));
        //暫定
        bundle.putString("code",sentCode);
        // 実行するメソッドを設定
        int loaderId = 0;
        switch (adapterView.getId()) {
            case R.id.PrefSpinner:
                loaderId = EkidataLoader.PREFECTURES;
                break;
            case R.id.LineSpinner:
                loaderId = EkidataLoader.LINES;
                break;
            case R.id.StaSpinner:
                loaderId = EkidataLoader.STATIONS;
                break;
        }
        //レファレンスに使っちゃダメとか書いてないのになんだお前ーーーーー
        getSupportLoaderManager().initLoader(loaderId, bundle, self);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @NonNull
    @Override
    public Loader<ArrayList<HashMap<String, String>>> onCreateLoader(int i, @Nullable Bundle bundle) {
        return null;
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<HashMap<String, String>>> loader,
                               ArrayList<HashMap<String, String>> list) {
        if (list == null) {
            // 何らかの理由により取得できない
            Toast.makeText(self, "エラー", Toast.LENGTH_SHORT).show();
            return;
        }

        // 取得したデータを対象のSpinnerにセットする
        SimpleAdapter adapter = new SimpleAdapter(
                self, list, android.R.layout.simple_spinner_item,
                new String[] {"code"}, new int[] {android.R.id.text1});
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        switch (loader.getId()) {
            case EkidataLoader.PREFECTURES:
                //配列をつくってEkidataのstationいれて
                lineSpinner.setAdapter(adapter);
                lineSpinner.setOnItemSelectedListener(self);
                break;
            case EkidataLoader.LINES:
                staSpinner.setAdapter(adapter);
                break;
        }

    }

    @Override
    public void onLoaderReset(@NonNull Loader<ArrayList<HashMap<String, String>>> loader) {
        return;
    }
}
