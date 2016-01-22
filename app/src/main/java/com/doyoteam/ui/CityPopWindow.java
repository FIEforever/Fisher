package com.doyoteam.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.doyoteam.fisher.Constants;
import com.doyoteam.fisher.R;
import com.doyoteam.fisher.data.CityData;
import com.doyoteam.fisher.db.bean.City;
import com.doyoteam.fisher.db.dao.CityDao;
import com.doyoteam.fisher.mall.CityPicker;

import java.util.ArrayList;
import java.util.List;

/**
 * 弹出县级城市的PopWindow
 *
 * @author PangXuan (pangxuan2010@gmail.com)
 * @version Post 210
 *          Datetime 2015-11-17 17:36
 *          Copyright 2015 东莞市邮政公司电子商务分局 All rights reserved.
 * @since Post 1.0
 */
public class CityPopWindow implements View.OnClickListener, AdapterView.OnItemClickListener {

    private Context mContext;
    private Fragment mFragment;
    private LayoutInflater mInflater;
    private OnWindowListener mListener;

    private BaseAdapter adapter;
    private List<City> countyList;
    private City curCity;
    private City curCounty;

    private TextView tv_city;
    private TextView tv_current_city;
    private PopupWindow popupWindow;
    private View rootView;
    private View mAnchor;

    public CityPopWindow(Fragment fragment, View anchor) {
        this.mContext = fragment.getActivity();
        this.mFragment = fragment;
        this.mInflater = LayoutInflater.from(mContext);
        rootView = mInflater.inflate(R.layout.window_city, null, false);
        this.curCity = CityData.getInstance().currentCity;
        this.countyList = new ArrayList<>();
        this.mAnchor = anchor;

        if (CityData.getInstance().currentCounty != 0)
            curCounty = CityDao.getInstance().getCity(CityData.getInstance().currentCounty);

        tv_city = (TextView) anchor;
        tv_current_city = (TextView) rootView.findViewById(R.id.city_current);
        GridView gv_city_picker = (GridView) rootView.findViewById(R.id.city_grid);
        rootView.findViewById(R.id.city_change).setOnClickListener(this);

        adapter = new BaseAdapter() {

            @Override
            public int getCount() {
                if (countyList == null) return 0;
                return countyList.size();
            }

            @Override
            public City getItem(int position) {
                return countyList.get(position);
            }

            @Override
            public long getItemId(int position) {
                return getItem(position).id;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                TextView tv_county;
                if (convertView == null) {
                    convertView = mInflater.inflate(R.layout.item_grid_city, parent, false);
                }
                tv_county = (TextView) convertView;
                tv_county.setText(getItem(position).name);
                if (CityData.getInstance().currentCounty == (int) getItemId(position))
                    tv_county.setBackgroundResource(R.drawable.bg_button_city_checked);
                else tv_county.setBackgroundResource(R.drawable.bg_button_city_normal);
                return convertView;
            }
        };
        gv_city_picker.setAdapter(adapter);
        gv_city_picker.setOnItemClickListener(this);
    }

    public City getShowCity() {
        if (curCounty != null) return curCounty;
        else return curCity;
    }

    public void updateCity(City currentCity) {
        if (currentCity.id != curCity.id) {
            curCity = currentCity;
            CityDao.getInstance().updateCurrentArea(curCity);
            CityData.getInstance().syncCurrentCity(curCity);

            tv_city.setText(curCity.name);

            countyList.clear();
            countyList.add(0, new City(0, "全" + curCity.name.substring(curCity.name.length() - 1,
                    curCity.name.length())));
            countyList.addAll(CityDao.getInstance().getChildCity(curCity.id));

            adapter.notifyDataSetChanged();
            tv_current_city.setText("当前城市：" + curCity.name);

            // 更新广告位图标
//            gainAdData();
        }
    }

    public void show() {
        tv_city.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_arrow_up_white, 0);
        tv_current_city.setText("当前城市：" + curCity.name);
        countyList.clear();
        countyList.add(0, new City(0, "全" + curCity.name.substring(curCity.name.length() - 1,
                curCity.name.length())));
        countyList.addAll(CityDao.getInstance().getChildCity(curCity.id));
        adapter.notifyDataSetChanged();
        if (mListener != null) mListener.onAppear();

        popupWindow = new PopupWindow(rootView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup
                .LayoutParams.WRAP_CONTENT, true) {
            @Override
            public void dismiss() {
                if (mListener != null) {
                    mListener.onDisappear();
                }
//                    tv_city.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable
// .ic_arrow_down, 0);
//                    City curCity = param.getCurrentCity();
//                    if (curCity.id != param.city.id) {
//                        param.city = curCity;
//                        param.curPage = 0;
//                        DataModel.getInstance().dispatch(new MessageEvent(Constants
// .METHOD_CHANGE_CITY, null));
//                        initHomeData();
//                    }
                tv_city.setText(getShowCity().name);
                tv_city.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable
                        .ic_arrow_down_white, 0);
                super.dismiss();
            }
        };
        popupWindow.setAnimationStyle(0);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.showAsDropDown(mAnchor);
        popupWindow.update();
    }

    public interface OnWindowListener {

        void onAppear();

        void onDisappear();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.city_change:
                Intent intent = new Intent(mContext, CityPicker.class);
                mFragment.startActivityForResult(intent, Constants.REQUEST_CODE_CITY);
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (position == 0) {
            CityData.getInstance().syncCurrentCounty(0);
            curCounty = null;
        } else if ((int) id != CityData.getInstance().currentCounty) {
            CityData.getInstance().syncCurrentCounty((int) id);
            curCounty = countyList.get(position);
        }

        popupWindow.dismiss();
    }
}
