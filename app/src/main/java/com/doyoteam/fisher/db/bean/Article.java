package com.doyoteam.fisher.db.bean;

import java.io.Serializable;

/**
 * 文章基类
 * Created by F on 2015/12/21.
 */
public class Article implements Serializable, Cloneable  {

    //类型
    public static final int NEW = 1;
    public static final int ABC  = 2;
    public static final int SKILL   = 3;
    public static final int BAIT  = 4;
    public static final int VIDEO   = 5;

    public String id;
    public String typeid;
    public String channel;
    public String title;
    public String writer;
    public String litpic;
    public String pubdate;
    public String description;
    public String click;
    public String goodpost;
    public String nid;
    public String skin;
    public String senddate;
    public String detail_url;
    public String notpost;
    public String body;
    public String source;

    @Override
    public String toString() {
        return title;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}

