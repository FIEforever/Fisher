package com.doyoteam.fisher.home;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.doyoteam.fisher.Constants;
import com.doyoteam.fisher.R;
import com.doyoteam.fisher.db.bean.Post;
import com.doyoteam.util.Tools;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by F on 2015/12/13.
 */
public class PostAdapter extends BaseAdapter {

    private final LayoutInflater mLayoutInflater;
    private final Context mContext;
    private ArrayList<Post> arrayList;

    public PostAdapter(Context context) {
        this.mContext = context;
        this.mLayoutInflater = LayoutInflater.from(context);
    }

    public void setData(ArrayList<Post> data)
    {
        arrayList = data;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        int count = 0;
        if(arrayList != null)
        {
            count = arrayList.size();
        }
        return count;
    }

    @Override
    public Object getItem(int position) {
        if(arrayList != null)
        {
            return  arrayList.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        if(view == null)
        {
//            view = mLayoutInflater.inflate(R.layout.item_car_list, null);
            new ViewHolder(view);
        }
        final ViewHolder holder = (ViewHolder) view.getTag();
        //配置赋值
        Tools.renderBackground(mContext, holder.item_ll, R.drawable.bg_material_button_white);

        return view;
    }

    class ViewHolder {
        private RelativeLayout item_ll;//这个布局
        private ImageView item_car_icon;//车辆图标
        private TextView item_car_number;//车牌
        private TextView item_car_date;//车险到期日期
        private TextView item_state_traffic;//新违章数
        private TextView item_state_do;//待处理数

        public ViewHolder(View view) {
//            item_ll = (RelativeLayout)view.findViewById(R.id.item_ll);
//            item_car_icon = (ImageView)view.findViewById(R.id.item_car_icon);
//            item_car_number = (TextView)view.findViewById(R.id.item_car_number);
//            item_car_date = (TextView)view.findViewById(R.id.item_car_date);
//            item_state_traffic = (TextView)view.findViewById(R.id.item_state_traffic);
//            item_state_do = (TextView)view.findViewById(R.id.item_state_do);
            view.setTag(this);
        }
    }
}
