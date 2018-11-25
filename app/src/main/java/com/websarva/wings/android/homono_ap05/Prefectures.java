package com.websarva.wings.android.homono_ap05;

import java.util.Objects;

public class Prefectures {

    //変数を右クリックgenerateしてgetterとsetterとかが作れる
    private String id;
    private String name;

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

    //コンストラクタ
    public Prefectures(String id,String name){
        this.id = id;
        this.name= name;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Prefectures){
            Prefectures prefectures = (Prefectures) o;
            if (prefectures.getName().equals(name) && prefectures.getId() == id) return true;
        }
        return false;
    }

}
