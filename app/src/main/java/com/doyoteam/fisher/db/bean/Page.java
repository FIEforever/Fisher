package com.doyoteam.fisher.db.bean;

import java.io.Serializable;

/**
 * 分页基类
 * Created by F on 2015/12/21.
 */
public class Page implements Serializable, Cloneable  {

    public String totalCount;
    public String pageCount;
    public String nextPage;
    public String perPage;

    @Override
    public String toString() {
        return perPage;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}

