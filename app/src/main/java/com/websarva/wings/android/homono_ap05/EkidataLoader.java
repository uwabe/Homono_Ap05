package com.websarva.wings.android.homono_ap05;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

//非同期通信クラス
public class EkidataLoader extends AsyncTask<String, String, String> {

    private StationSearch _ss;
    private String spinner_name = "";

    // コンストラクタ
    public EkidataLoader(StationSearch ss) {
        super();
        _ss = ss;
    }

    @Override
    protected String doInBackground(String... strings) {
        //スピナーの名前が0番目
        spinner_name = strings[0];
        String jsonData = "";
        try {
            //インターネット接続開始
            HttpURLConnection httpURLConnection = null;
            URL url = null;
            String urlDraft = "";
            //タップされた名前のidが1番目
            if (spinner_name == "都道府県") {
                urlDraft = "http://www.ekidata.jp/api/p/" + strings[1] + ".json";
            } else if (spinner_name == "路線") {
                urlDraft = "http://www.ekidata.jp/api/l/" + strings[1] + ".json";
                Log.e("data", "urlは"+urlDraft);
            }
            url = new URL(urlDraft);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            //ここ調べる
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setInstanceFollowRedirects(false);
            httpURLConnection.setDoInput(true);
            //接続開始
            httpURLConnection.connect();
            InputStream inputStream = httpURLConnection.getInputStream();
            jsonData = is2String(inputStream);

            //取得したjsonを読み取れるように改造する
            //\はエスケープ
            if (spinner_name == "都道府県") {
                jsonData = jsonData.replace("if(typeof(xml)=='undefined') xml = {};xml.data =", "");
                jsonData = jsonData.replace("if(typeof(xml.onload)=='function') xml.onload(xml.data);", "");
                jsonData = jsonData.replace("\"line\"", "line");
                jsonData = jsonData.replace("\"line_cd\"", "line_cd");
                jsonData = jsonData.replace("\"line_name\"", "line_name");
                jsonData = jsonData.replace("line_cd:", "line_cd:\"");
                jsonData = jsonData.replace(",line_name:", "\",line_name:");
                Log.d("Data",jsonData);
            } else if (spinner_name == "路線") {
                Log.d("Data",jsonData);
                jsonData = jsonData.replace("if(typeof(xml)=='undefined') xml = {};xml.data = ", "");
                jsonData = jsonData.replace("if(typeof(xml.onload)=='function') xml.onload(xml.data);", "");
                jsonData = jsonData.replace("\"station_l\"", "station_l");
                jsonData = jsonData.replace("\"station_cd\"", "station_cd");
                jsonData = jsonData.replace("\"station_name\"", "station_name");
                Log.d("Data",jsonData);
            }

            httpURLConnection = null;
            url = null;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonData;
    }

    @Override
    protected void onPostExecute(String result) {
        _ss.result_job(spinner_name, result);
    }

    private String is2String(InputStream inputStream) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
        StringBuffer stringBuffer = new StringBuffer();
        String st1 = "";
        while ((st1 = reader.readLine()) != null) {
            stringBuffer.append(st1);
        }
        try {
            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stringBuffer.toString();
    }

}
