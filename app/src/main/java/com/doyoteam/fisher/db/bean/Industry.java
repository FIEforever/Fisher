package com.doyoteam.fisher.db.bean;

/**
 * 行业对象
 * Created by F on 2015/12/21.
 */
public class Industry implements Cloneable {

    public final static int ROOT_ID = 0;

    public int id;          // 行业ID
    public String name;     // 行业名
    public int pid;         // 行业父ID
    public int weight;      // 排序权值

    @Override
    public String toString() {
        return name;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
