package com.doyoteam.fisher.db.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 文章基类
 * Created by F on 2015/12/21.
 */
public class Classify implements Serializable, Cloneable  {


    public String id;
    public String title;
    public ArrayList<Classify> classify_list;

    @Override
    public String toString() {
        return title;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}

