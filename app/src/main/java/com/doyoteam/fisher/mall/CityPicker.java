package com.doyoteam.fisher.mall;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.doyoteam.fisher.R;
import com.doyoteam.fisher.data.CityData;
import com.doyoteam.fisher.db.bean.City;
import com.doyoteam.fisher.db.dao.CityDao;
import com.doyoteam.util.SlideBackActivity;
import com.doyoteam.util.Tools;

import java.util.List;


public class CityPicker extends SlideBackActivity implements OnItemClickListener, View.OnClickListener {

    private static final int PICKER_CITY = 0;

    private List<City> beanList;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_bar_return:
                finish();
                break;
        }
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picker_city);
        TextView title = (TextView) findViewById(R.id.title_bar_title);
        ListView lv_province = (ListView) findViewById(R.id.list);
        findViewById(R.id.title_bar_return).setOnClickListener(this);

        int id = getIntent().getIntExtra("PROVINCE_ID", 0);
        if (id == 0) {
            title.setText(R.string.activity_province);
            beanList = CityDao.getInstance().getChildCity(CityData.DEFAULT_PID);
        } else {
            title.setText(R.string.activity_city);
            beanList = CityDao.getInstance().getChildCity(id);
        }
        lv_province.setAdapter(new CityAdapter(this, beanList));
        lv_province.setOnItemClickListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == PICKER_CITY) returnResult(data);
    }

    private void returnResult(City city) {
        Intent data = new Intent();
        data.putExtra("CITY", city);
        setResult(RESULT_OK, data);
        finish();
    }

    private void returnResult(Intent data) {
        setResult(RESULT_OK, data);
        finish();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        City bean = beanList.get(position);
        if (bean.area == null) {    // 进入市级选择
            Tools.showToast("无此地区数据");
        } else if (bean.area.length() < 6) {    // 进入市级
            Intent intent = new Intent(this, CityPicker.class);
            intent.putExtra("PROVINCE_ID", bean.id);
            startActivityForResult(intent, PICKER_CITY);
        } else {                            // 返回市级数据
            returnResult(bean);
        }
    }
}

class CityAdapter extends BaseAdapter {

    private List<City> cityList;
    private LayoutInflater inflater;

    public CityAdapter(Context context, List<City> cityList) {
        this.inflater = LayoutInflater.from(context);
        this.cityList = cityList;
    }

    @Override
    public int getCount() {
        return cityList.size();
    }

    @Override
    public City getItem(int position) {
        return cityList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder;
        City city = getItem(position);

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_city, parent, false);
            holder = new Holder();
            holder.tv_name = (TextView) convertView.findViewById(R.id.city_name);
            holder.tv_state = (TextView) convertView.findViewById(R.id.city_state);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        holder.tv_name.setText(city.name);

        StringBuilder builder = new StringBuilder();
        if (city.count / 10000 > 0) builder.append("当前");
        if (((city.count % 10000) / 1000) > 0) {
            if(builder.length() > 0)
                builder.append("/");
            builder.append("定位");
        }
        if (builder.length() > 0) {
            holder.tv_state.setVisibility(View.VISIBLE);
            holder.tv_state.setText(builder.append("城市").toString());
        } else
            holder.tv_state.setVisibility(View.INVISIBLE);
        return convertView;
    }

    class Holder {
        TextView tv_name;
        TextView tv_state;
    }
}
