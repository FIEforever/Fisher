package com.doyoteam.fisher.db.bean;

import java.io.Serializable;

/**
 * 视频类
 * Created by F on 2016/1/16.
 */
public class Video implements Serializable, Cloneable  {


    public String name;
    public String player_type;
    public String url;
    public String type;
    public String sub_item;

    @Override
    public String toString() {
        return name;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}

