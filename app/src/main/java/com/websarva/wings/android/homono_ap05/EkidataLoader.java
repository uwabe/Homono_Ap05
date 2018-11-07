package com.websarva.wings.android.homono_ap05;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class EkidataLoader extends AsyncTaskLoader<ArrayList<HashMap<String, String>>> {

    /** 都道府県ID. */
    public static final int PREFECTURES = 1;
    /** 路線ID. */
    public static final int LINES = 2;
    /** 駅ID. */
    public static final int STATIONS = 3;

    /** Logcat出力用タグ. */
    private static final String TAG = EkidataLoader.class.getSimpleName();

    /** パラメータにつける名前(都道府県or路線). */
    private String pCode;

    // コンストラクタ
    public EkidataLoader(Context context, String code){
        super(context);
        pCode = code;
        forceLoad();
    }

    @Override
    public ArrayList<HashMap<String, String>> loadInBackground() {
        ArrayList<HashMap<String,String>> list = new ArrayList<HashMap<String, String>>();
        JSONObject jsonObject = get(pCode);

        if(jsonObject != null){
            /* "選択してください"アイテムを先頭に追加
            HashMap<String, String> notSelected = new HashMap<String, String>();
            notSelected.put("name", "選択してください");
            list.add(notSelected);   */
            try{
                //取得したJSONからデータをとりだす
                //緑字のところは外部APIの名前のキーを
                //JSONObject response = jsonObject.getJSONObject("line");
                JSONArray jsonArray = jsonObject.getJSONArray("line");
                //JSONArray jsonArray = null;
                switch (getId()){
                    case PREFECTURES:
                        //jsonArray = response.getJSONArray("line_cd");
                        for(int i =0; i< jsonArray.length(); i++){
                            HashMap<String,String> station = new HashMap<String, String>();
                            JSONObject data = jsonArray.getJSONObject(i);
                            String line_cd = data.getString("line_cd");
                            String line_name = data.getString("line_name");
                            station.put(line_cd,line_name);
                            //station.put("line_cd",jsonArray.getString(i));
                            //station.put("line_name",jsonArray.getString(i));
                            list.add(station);
                        }
                        break;
                        //都道府県ができたらいい感じに書き直す
                    case LINES:
                        //jsonArray = response.getJSONArray("station_l");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            HashMap<String, String> station = new HashMap<String, String>();
                            station.put("name", jsonArray.getString(i));
                            list.add(station);
                        }
                        break;
                    case STATIONS:
                        //jsonArray =response.getJSONArray("station");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject item = jsonArray.getJSONObject(i);
                            HashMap<String, String> station = new HashMap<String, String>();
                            station.put("name", item.getString("name"));
                            station.put("latitude", item.getString("y"));
                            station.put("longitude", item.getString("x"));
                            list.add(station);
                        }
                        break;
                }
            } catch(Exception e){
                return  null;
            }
        }
        return list;
    }

    /**
     * Getリクエストを実行してBodyを取得する.
     * @param code パラメータにつける名前(エリアor都道府県or路線)
     * @return JSONObject
     */
    private JSONObject get(String code) {
        HttpURLConnection httpURLConnection = null;
        InputStream inputStream = null;
        String urlStr = "http://www.ekidata.jp/api/" + getParams(code) + ".json";
        try {
            URL url = new URL(urlStr);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.connect();
            inputStream = httpURLConnection.getInputStream();
            String body = is2String(inputStream);
        }
        catch(MalformedURLException e){
        }
        catch (IOException e){
        }
        finally {
            if(httpURLConnection != null){
                httpURLConnection.disconnect();
            }
            if(inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                }
            }
        }return new JSONObject();
    }

    /**
     * URLパラメータを返す.
     * @param code パラメータにつける名前(エリアor都道府県or路線)
     * @return URLパラメータ
     */
    private String getParams(String code) {
        switch (getId()) {
            case PREFECTURES:
                return "/p/" + code;
            case LINES:
                return "/l/" + code;
            case STATIONS:
                return "/s/" + code;
        }
        return null;
    }

    private String is2String(InputStream inputStream) throws IOException{
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
        StringBuffer stringBuffer = new StringBuffer();
        char[] b = new char[1024];
        int lines;
        while (0 <= (lines = reader.read(b))){
            stringBuffer.append(b,0,lines);
        }
        return stringBuffer.toString();
    }
}
