package com.doyoteam.fisher.data;

import com.doyoteam.fisher.R;

import java.util.HashMap;

/**
 * Created by xiaogang on 2015/12/9.
 */
public class RecordData {
    public static HashMap<Integer,Integer> typeMap = new HashMap<>();
    public static HashMap<Integer,Integer> typeMap2 = new HashMap<>();
    public RecordData(){
        typeMap.put(1, R.drawable.icon_xiche2);
        typeMap2.put(1, R.drawable.icon_xiche);

    }
}
