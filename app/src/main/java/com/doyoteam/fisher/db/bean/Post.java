package com.doyoteam.fisher.db.bean;

import java.io.Serializable;

/**
 * 帖子基类
 * Created by F on 2015/12/21.
 */
public class Post implements Serializable, Cloneable  {

    public String forum_id;
    public String name;
    public String target;
    public String type;
    public String allow_post;
    public String desc;
    public String icon;

    @Override
    public String toString() {
        return name;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}

