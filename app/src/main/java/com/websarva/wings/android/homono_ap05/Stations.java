package com.websarva.wings.android.homono_ap05;

public class Stations {
    //変数を右クリックgenerateしてgetterとsetterとかが作れる
    private String id;
    private String name;
    private String lon;
    private String lat;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    @Override
    public String toString() {
        return name;
    }

    //コンストラクタ
    public Stations(String id, String name, String lon, String lat) {
        this.id = id;
        this.name = name;
        this.lon = lon;
        this.lat = lat;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Stations) {
            Stations stations = (Stations) o;
            if (stations.getName().equals(name) && stations.getId() == id) return true;
        }
        return false;
    }

}
