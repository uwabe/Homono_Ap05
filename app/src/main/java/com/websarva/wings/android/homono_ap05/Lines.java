package com.websarva.wings.android.homono_ap05;

public class Lines {

    //変数を右クリックgenerateしてgetterとsetterとかが作れる
    private String id;
    private String name;

    //コンストラクタ
    public Lines(String id,String name){
        this.id = id;
        this.name= name;
    }

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

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Lines){
            Lines lines = (Lines) o;
            if (lines.getName().equals(name) && lines.getId() == id) return true;
        }
        return false;
    }

}
