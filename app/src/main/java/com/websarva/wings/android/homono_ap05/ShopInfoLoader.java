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

public class ShopInfoLoader extends AsyncTask<String,String,String> {

    private PopupInfo _pi;
    private String place_id;

    //コンストラクタ
    public ShopInfoLoader(PopupInfo pi){
        super();
        _pi=pi;
    }

    @Override
    protected String doInBackground(String... strings){
        place_id = strings[0];
        Log.d("★place_id", strings[0]);
        String readStr = "";
        try{
            HttpURLConnection httpURLConnection = null;
            URL url = null;

            String urlStr = "https://maps.googleapis.com/maps/api/place/details/json?placeid=" + place_id + "&=name,rating,formatted_phone_number&key=*";
            Log.d("★JSONのURL", urlStr);
            url = new URL(urlStr);
            httpURLConnection = (HttpURLConnection)url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setInstanceFollowRedirects(false);
            httpURLConnection.setDoInput(true);

            httpURLConnection.connect();
            InputStream inputStream = httpURLConnection.getInputStream();
            readStr = is2String(inputStream);
            Log.i("★取得結果",readStr);
        }catch (MalformedURLException e) {
            e.printStackTrace();
        }catch (Exception ex) {
            ex.printStackTrace();
        }
        return readStr;
    }

    @Override
    protected void onPostExecute(String result) {
        Log.d("★取得したデータ", result);
        _pi.result_job(result);
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

