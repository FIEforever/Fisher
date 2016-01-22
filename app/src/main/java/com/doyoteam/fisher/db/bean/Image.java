package com.doyoteam.fisher.db.bean;

import java.io.Serializable;

/**
 * 图片类
 * Created by F on 2015/12/21.
 */
public class Image implements Serializable, Cloneable  {


    public String img_mark;
    public String url;

    @Override
    public String toString() {
        return img_mark;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}

