package com.websarva.wings.android.homono_ap05;

import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends FragmentActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private final MainActivity self = this;
    //private ProgressBar progressBar;

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
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
