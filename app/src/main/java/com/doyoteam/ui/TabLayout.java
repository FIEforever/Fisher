package com.doyoteam.ui;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.CheckedTextView;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.doyoteam.fisher.R;
import com.doyoteam.fisher.data.CityData;
import com.doyoteam.fisher.db.bean.City;
import com.doyoteam.fisher.db.bean.Industry;
import com.doyoteam.fisher.db.bean.ShopParam;
import com.doyoteam.fisher.db.dao.CityDao;
import com.doyoteam.fisher.db.dao.IndustryDao;

import java.util.ArrayList;
import java.util.List;


/**
 * diy Tab Host Widget
 *
 * @author PangXuan (pangxuan2010@gmail.com)
 * @version NearShop 4.0
 *          Datetime 2015-05-19 08:54
 *          Copyright 2015 东莞邮政电子商务分局 All rights reserved.
 * @since NearShop 4.0
 */
public class TabLayout extends LinearLayout implements View.OnClickListener, ToggleButton
        .OnCheckedChangeListener, AdapterView.OnItemClickListener {

    private ShopParam shopParam;
    private OnDismissListener onDismissListener;
    private boolean tabType;        // true Shop; false Group

//    public static final int LAYOUT_HALF = 2;
//    public static final int LAYOUT_FULL = 1;

    private Context mContext;
    private LayoutInflater inflater;
    private DisplayMetrics metrics;
    private List<ToggleButton> viewList;
    private ListView lv_primary;        // 一级列表
    private ListView lv_secondary;      // 二级列表
    private GridView gridView;          // 套餐人数筛选
    private CheckedTextView ctv1;        // 套餐筛选1
    private CheckedTextView ctv2;        // 套餐筛选2
    private CheckedTextView ctv3;        // 套餐筛选3
    private PopupWindow popupWindow;
    private TabParam[] paramArray;
    private int curPosition;
    private boolean isNetParamUpdate;

    private List<City> countyList;
    private List<Industry> industryList;
    private List<Industry> subIndustries;
    private int primaryPosition;
    private String[] orderShopType;
    private String[] orderGroupType;
    private String[] shopModeType;
    private String[] peopleAmount;

    public TabLayout(Context context) {
        super(context);
        init(context);
    }

    public TabLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    // 更新城市数据
    public void updateCityData() {
        City curCity = CityData.getInstance().currentCity;
        countyList = CityDao.getInstance().getChildCity(CityData.getInstance().currentCity.id);

        City topCity;
        try {
            topCity = (City) curCity.clone();
        } catch (CloneNotSupportedException e) {
            topCity = CityDao.getInstance().getCity(curCity.id);
            e.printStackTrace();
        }
        topCity.name = "全" + curCity.name.substring(curCity.name.length() - 1, curCity.name
                .length());
        countyList.add(0, topCity);
        viewList.get(1).setText(topCity.name);
        shopParam.city = topCity;

        if(lv_primary != null)
            lv_primary.setAdapter(new ArrayAdapter<>(mContext, R.layout.item_tab_city, R
                    .id.item_tab_city, countyList));
    }

    private void init(Context context) {
        mContext = context;
        inflater = LayoutInflater.from(context);
        metrics = context.getResources().getDisplayMetrics();
        setOrientation(LinearLayout.HORIZONTAL);

        City curCity = CityData.getInstance().currentCity;
        countyList = CityDao.getInstance().getChildCity(CityData.getInstance().currentCity.id);

        City topCity;
        try {
            topCity = (City) curCity.clone();
        } catch (CloneNotSupportedException e) {
            topCity = CityDao.getInstance().getCity(curCity.id);
            e.printStackTrace();
        }
        topCity.name = "全" + curCity.name.substring(curCity.name.length() - 1, curCity.name
                .length());
        countyList.add(0, topCity);

        industryList = IndustryDao.getInstance().getChildren(Industry.ROOT_ID);
        orderShopType = context.getResources().getStringArray(R.array.order_shop_type);
        orderGroupType = context.getResources().getStringArray(R.array.order_group_type);
        shopModeType = context.getResources().getStringArray(R.array.shop_mode_type);
        peopleAmount = context.getResources().getStringArray(R.array.other_people_amount);

        curPosition = -1;
        isNetParamUpdate = false;
        primaryPosition = 0;
    }

    public void setOnDismissListener(OnDismissListener onDismissListener) {
        this.onDismissListener = onDismissListener;
    }

    // 初始化列表
    public void initView(TabParam[] paramArray, ShopParam param, boolean isShop) {

        this.tabType = isShop;
        this.paramArray = paramArray;
        this.shopParam = param;

        LayoutParams lp = new LayoutParams(0, LayoutParams.MATCH_PARENT, 1);
        LayoutParams lpDivider = new LayoutParams((int) (metrics.density + 0.5),
                LayoutParams.MATCH_PARENT);
        lpDivider.setMargins(0, (int) (8 * metrics.density + 0.5), 0, (int) (8 * metrics.density
                + 0.5));
        viewList = new ArrayList<>();
        for (int i = 0; i < paramArray.length; i++) {
            if (i > 0) {
                View divider = new View(mContext);
                divider.setBackgroundColor(getResources().getColor(R.color.background_app));
                addView(divider, lpDivider);
            }
            FrameLayout container = (FrameLayout) inflater.inflate(R.layout.item_tab, null, false);
            ToggleButton button = (ToggleButton) container.getChildAt(0);
            button.setOnCheckedChangeListener(this);
            button.setTag(i);
            button.setText(paramArray[i].title);
            viewList.add(button);
            addView(container, lp);
        }
    }

    // 设置指定Tab位置的文字
    public void setTabText(int position, String text) {
        if (position > viewList.size())
            throw new ArrayIndexOutOfBoundsException("position:" + position + "out of bound:" +
                    viewList.size());
        viewList.get(position).setText(text);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            curPosition = (int) buttonView.getTag();
            popWindow(buttonView);
        }
    }

    // 弹出选择框
    private void popWindow(final CompoundButton anchor) {
        TabParam tabParam = paramArray[curPosition];    // 当前Tab类型
        View rootView;

//        if (tabParam.type < 4)
         {
            rootView = inflater.inflate(R.layout.window_list, null, false);
            lv_primary = (ListView) rootView.findViewById(R.id.window_list_primary);
            switch (tabParam.type) {
                case TabParam.TYPE_INDUSTRY:
                    lv_secondary = (ListView) rootView.findViewById(R.id.window_list_secondary);
                    lv_secondary.setVisibility(INVISIBLE);
                    rootView.setBackgroundResource(android.R.color.transparent);
                    lv_primary.setAdapter(new BaseAdapter() {
                        @Override
                        public int getCount() {
                            if (industryList == null) return 0;
                            return industryList.size();
                        }

                        @Override
                        public Industry getItem(int position) {
                            return industryList.get(position);
                        }

                        @Override
                        public long getItemId(int position) {
                            return industryList.get(position).id;
                        }

                        @Override
                        public View getView(int position, View convertView, ViewGroup parent) {
                            Industry industry = getItem(position);
                            if (convertView == null)
                                convertView = inflater.inflate(R.layout.item_tab_city, parent, false);
                            TextView textView = (TextView) convertView;
                            textView.setText(industry.name);

                            if (primaryPosition == 0 && industry.id == shopParam
                                    .getIndustryPrimaryId()) {
                                textView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable
                                        .ic_togo_tiny_primary, 0);
                                textView.setTextColor(getResources().getColor(R.color.primary));
                            } else if (primaryPosition != 0 && position == primaryPosition) {
                                textView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable
                                        .ic_togo_tiny_primary, 0);
                                textView.setTextColor(getResources().getColor(R.color.primary));
                            } else {
                                textView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable
                                        .ic_togo, 0);
                                textView.setTextColor(getResources().getColor(R.color
                                        .secondary_text_dark));
                            }

                            if (position == 0)
                                textView.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                            return convertView;
                        }
                    });
                    lv_primary.setBackgroundResource(R.drawable.window_background);
                    rootView.findViewById(R.id.window_list_divider).setVisibility(INVISIBLE);
                    rootView.setClickable(true);
                    rootView.setOnClickListener(this);
                    break;

                case TabParam.TYPE_AREA:
                    lv_primary.setAdapter(new ArrayAdapter<>(mContext, R.layout.item_tab_city, R
                            .id.item_tab_city, countyList));
                    break;

                case TabParam.TYPE_ORDER:
                    if(tabType)
                        lv_primary.setAdapter(new ArrayAdapter<>(mContext, R.layout.item_tab_city, R
                                .id.item_tab_city, orderShopType));
                    else
                        lv_primary.setAdapter(new ArrayAdapter<>(mContext, R.layout.item_tab_city, R
                                .id.item_tab_city, orderGroupType));
                    break;

                case TabParam.TYPE_MODE:
                    lv_primary.setAdapter(new ArrayAdapter<>(mContext, R.layout.item_tab_city, R
                            .id.item_tab_city, shopModeType));
                    break;

            }
            lv_primary.setOnItemClickListener(this);
        }

        // 设置Pop Window最大高值
        int curHeight = ViewGroup.LayoutParams.WRAP_CONTENT;

        if (tabParam.type == TabParam.TYPE_INDUSTRY)
            curHeight = (int) (industryList.size() * 41 * metrics.density + 0.5) + (int) (8 *
                    metrics.density);
        else if (tabParam.type == TabParam.TYPE_AREA) {
            curHeight = (int) (countyList.size() * 41 * metrics.density + 0.5);
            if (curHeight > 0.6 * metrics.heightPixels) {
                curHeight = (int) (0.6 * metrics.heightPixels + 0.5);
                curHeight = curHeight - curHeight % (int) (41 * metrics.density + 0.5) + (int) (6
                        * metrics.density);

            } else {
                curHeight = ViewGroup.LayoutParams.WRAP_CONTENT;
            }
        }

        popupWindow = new PopupWindow(rootView, metrics.widthPixels / tabParam.layoutWidth,
                curHeight, true) {
            @Override
            public void dismiss() {
                super.dismiss();
                anchor.toggle();
                curPosition = -1;
                primaryPosition = 0;
                if(onDismissListener != null)
                    onDismissListener.dismiss(isNetParamUpdate);
                isNetParamUpdate = false;
             }
        };
        popupWindow.setAnimationStyle(0);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.showAsDropDown(anchor, -anchor.getWidth(), 1);
        popupWindow.update();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.window_list:
                popupWindow.dismiss();
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (curPosition != -1) {
            TabParam param = paramArray[curPosition];
            switch (param.type) {
                case TabParam.TYPE_INDUSTRY:    // 行业数据发生变化
                    if (parent == lv_primary) {      // 主行业
                        if (position == 0) {         // 全部
                            param.title = "全部";
                            viewList.get(curPosition).setText(param.title);
                            // 是否有数据更新
                            if (shopParam.industryId != industryList.get(position).id) {
                                shopParam.industryId = industryList.get(position).id;
                                isNetParamUpdate = true;
                            }
                            popupWindow.dismiss();
                        } else {    // 进入二级列表
                            primaryPosition = position;
                            param.title = industryList.get(position).toString();
                            subIndustries = IndustryDao.getInstance().getChildren(industryList
                                    .get(position).id);
                            Industry industry;
                            try {
                                industry = (Industry) industryList.get(position).clone();
                            } catch (CloneNotSupportedException e) {
                                industry = IndustryDao.getInstance().getData(industryList.get
                                        (position).id);
                            }
                            industry.name = "全部";
                            subIndustries.add(0, industry);
                            lv_secondary.setAdapter(new ArrayAdapter<>(mContext, R.layout
                                    .item_tab_city, R.id.item_tab_city, subIndustries));
                            lv_secondary.setVisibility(VISIBLE);
                            lv_secondary.setOnItemClickListener(this);
                            lv_primary.setBackgroundResource(android.R.color.transparent);
                            View rootView = popupWindow.getContentView();
                            rootView.setBackgroundResource(R.drawable.window_background);
                            rootView.findViewById(R.id.window_list_divider).setVisibility(VISIBLE);
                            rootView.setClickable(false);
                            rootView.setOnClickListener(null);
                            ((BaseAdapter)lv_primary.getAdapter()).notifyDataSetChanged();
                            popupWindow.update();
                        }
                    } else {                    // 二级行业
                        if (position == 0) {
                            param.title = industryList.get(primaryPosition).name;
                        } else {
                            param.title = subIndustries.get(position).name;
                        }
                        viewList.get(curPosition).setText(param.title);

                        // 是否有数据更新
                        if (shopParam.industryId != subIndustries.get(position).id) {
                            shopParam.industryId = subIndustries.get(position).id;
                            isNetParamUpdate = true;
                        }
                        popupWindow.dismiss();
                    }
                    break;

                case TabParam.TYPE_AREA:        // 地区数据发生变化
                    City county = countyList.get(position);
                    if (county.id != shopParam.city.id) {
                        param.title = county.toString();
                        viewList.get(curPosition).setText(param.title);
                        shopParam.city = county;
                        isNetParamUpdate = true;
                    }
                    popupWindow.dismiss();
                    break;

                case TabParam.TYPE_ORDER:       // 排序数据发生变化
                    if(tabType)
                        param.title = orderShopType[position];
                    else
                        param.title = orderGroupType[position];
                    viewList.get(curPosition).setText(param.title);
                    if(shopParam.orderIndex != position) {
                        isNetParamUpdate = true;
                        shopParam.setOrderIndex(tabType, position);
                    }
                    popupWindow.dismiss();
                    break;

                case TabParam.TYPE_MODE:
                    param.title = shopModeType[position];
                    viewList.get(curPosition).setText(param.title);
                    if(shopParam.shopMode != position) {
                        isNetParamUpdate = true;
                        shopParam.shopMode = position;
                    }
                    popupWindow.dismiss();
                    break;

                case TabParam.TYPE_MORE:       //  筛选数据发生变化
                    shopParam.amountIndex = position;
                    isNetParamUpdate = true;
                    ((BaseAdapter) parent.getAdapter()).notifyDataSetChanged();
                    break;
            }
        }
    }

    public static class TabParam {

        // Tab数据类型
        public final static int TYPE_INDUSTRY = 0;
        public final static int TYPE_AREA = 1;
        public final static int TYPE_ORDER = 2;
        public final static int TYPE_MODE = 3;
        public final static int TYPE_MORE = 4;

        // Tab PopWindow宽类型
        public final static int WIDTH_FULL = 1;
        public final static int WIDTH_HALF = 2;

        public TabParam(int type, int layoutWidth, String title) {
            this.type = type;
            this.layoutWidth = layoutWidth;
            this.title = title;
        }

        int type;           // Tab 类型
        int layoutWidth;    // 宽类型  1全屏，2半屏
        String title;       // 默认标题
    }

    public interface OnDismissListener {
        void dismiss(boolean isNeedUpdate);
    }
}