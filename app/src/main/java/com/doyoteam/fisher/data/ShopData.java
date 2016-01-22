package com.doyoteam.fisher.data;

import android.util.SparseArray;

import com.doyoteam.fisher.db.bean.Shop;

import java.util.ArrayList;
import java.util.List;

/**
 * 商家列表数据
 *
 * @author PangXuan (pangxuan2010@gmail.com)
 * @version NearShop 4.0
 *          Datetime 2015-04-27 08:45
 *          Copyright 2015 东莞邮政电子商务分局 All rights reserved.
 * @since NearShop 4.0
 */
public class ShopData {

    /**
     * An array of items.
     */
    public static List<Shop> SHAKE_ITEMS = new ArrayList<>();
    public static SparseArray<Shop> SHAKE_MAP = new SparseArray<>();

    public static void resetShakeList(List<Shop> shakeList) {
        SHAKE_ITEMS.clear();
        SHAKE_MAP.clear();
        for(Shop shop: shakeList)
            addShakeItem(shop);
    }

    private static void addShakeItem(Shop item) {
        SHAKE_ITEMS.add(item);
        SHAKE_MAP.put(item.id, item);
    }
}
