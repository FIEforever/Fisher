package com.doyoteam.fisher.home;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.AbsoluteSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.doyoteam.fisher.Constants;
import com.doyoteam.fisher.R;
import com.doyoteam.fisher.db.bean.Ad;
import com.doyoteam.fisher.db.bean.Group;
import com.doyoteam.fisher.db.bean.Post;
import com.doyoteam.util.ImageLoaderConfig;
import com.doyoteam.util.ImageShowListener;
import com.doyoteam.util.Tools;
import com.doyoteam.drawables.Android;
import com.doyoteam.ui.AdControl;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * RecycleViewAdapter for Group
 */
public class GroupRvAdapter extends RecyclerView.Adapter<GroupRvAdapter.ViewHolder> {

    private final LayoutInflater mLayoutInflater;
    private final Context mContext;
    private List<Post> postList;
    private List<Ad> adList;
    private boolean isMall;
    private int position;

    public GroupRvAdapter(Context context, List<Post> postList, boolean isMall) {
        this.mContext = context;
        this.mLayoutInflater = LayoutInflater.from(context);
        this.postList = postList;
        this.isMall = isMall;
        this.position = -1;
        if (isMall) {
            this.adList = new ArrayList<>();
            adList.add(new Ad(R.drawable.pic_mall_ad1));
            adList.add(new Ad(R.drawable.pic_mall_ad2));
            adList.add(new Ad(R.drawable.pic_mall_ad3));
        }
    }

    public void update() {
        if(position >= 0) {
            adList.remove(position);
            position = -1;
            notifyDataSetChanged();
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (isMall) {
            if (position < 3) return position;
            else return 3;
        } else {
            return 3;
        }
    }

    @Override
    public GroupRvAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView;
        switch (viewType) {
            case 0:
                rootView = new AdControl(mContext);
                rootView.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams
                        .WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                ((AdControl) rootView).initData(adList, 750 / 340f);
                break;
            case 1:
                rootView = mLayoutInflater.inflate(R.layout.item_shop_classify, parent, false);
                break;
            case 2:
                rootView = mLayoutInflater.inflate(R.layout.item_shop_title, parent, false);
                break;
            default:
                rootView = mLayoutInflater.inflate(R.layout.item_post, parent, false);
                if (!Android.isLollipop())
                    Tools.renderBackground(mContext, rootView.findViewById(R.id
                            .item_product_layout), R.drawable.bg_material_button_grey);
                break;
        }
        return new ViewHolder(rootView, viewType);
    }

    @Override
    public void onBindViewHolder(GroupRvAdapter.ViewHolder holder, int position) {
        Post item;
        if (isMall) {
            if (position >= 3) {
                item = postList.get(position - 3);
            } else {
                return;
            }
        } else {
            item = postList.get(position);
        }
        ImageLoader.getInstance().displayImage(item.icon, holder.item_image1_iv,
                ImageLoaderConfig.init(Constants.IMAGE_DEFAULT_POST).getDisplayImageOptions(),
                new ImageShowListener());
        ImageLoader.getInstance().displayImage(item.icon, holder.item_image2_iv,
                ImageLoaderConfig.init(Constants.IMAGE_DEFAULT_POST).getDisplayImageOptions(),
                new ImageShowListener());
        ImageLoader.getInstance().displayImage(item.icon, holder.item_image3_iv,
                ImageLoaderConfig.init(Constants.IMAGE_DEFAULT_POST).getDisplayImageOptions(),
                new ImageShowListener());
        holder.item_title.setText(item.desc);
    }

    @Override
    public int getItemCount() {
        return postList.size() + (isMall ? 3 : 0);
    }

    @Override
    public long getItemId(int position) {
        if (isMall) {
            if (position < 3) return super.getItemId(position);
            else return Long.parseLong(postList.get(position - 3).forum_id);
        } else return Long.parseLong(postList.get(position).forum_id);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView item_title;
        ImageView item_image1_iv;
        ImageView item_image2_iv;
        ImageView item_image3_iv;
        ImageView item_user_iv;
        TextView item_user_name_tv;
        TextView item_user_city_tv;
        TextView item_comment_tv;
        TextView item_like_tv;

        public ViewHolder(View view, int viewType) {
            super(view);
            switch (viewType) {
                case 1:
                    view.findViewById(R.id.shop_classify1).setOnClickListener(this);
                    view.findViewById(R.id.shop_classify2).setOnClickListener(this);
                    view.findViewById(R.id.shop_classify3).setOnClickListener(this);
                    view.findViewById(R.id.shop_classify4).setOnClickListener(this);
                    view.findViewById(R.id.shop_classify5).setOnClickListener(this);
                    break;

                case 3:
                    view.findViewById(R.id.item_post_layout).setOnClickListener(this);
                    item_title = (TextView) view.findViewById(R.id.item_title);
                    item_image1_iv = (ImageView) view.findViewById(R.id.item_image1_iv);
                    item_image2_iv = (ImageView) view.findViewById(R.id.item_image2_iv);
                    item_image3_iv = (ImageView) view.findViewById(R.id.item_image3_iv);
                    item_user_iv = (ImageView) view.findViewById(R.id.item_user_iv);
                    item_user_name_tv = (TextView) view.findViewById(R.id.item_user_name_tv);
                    item_user_city_tv = (TextView) view.findViewById(R.id.item_user_city_tv);
                    item_comment_tv = (TextView) view.findViewById(R.id.item_comment_tv);
                    item_like_tv = (TextView) view.findViewById(R.id.item_like_tv);
                    break;
            }
        }

        @Override
        public void onClick(View v) {
            Intent intent;
            switch (v.getId()) {
                case R.id.item_product_layout:

                    break;
                case R.id.shop_classify1:
                    break;
                case R.id.shop_classify2:
                    break;
                case R.id.shop_classify3:
                    break;
                case R.id.shop_classify4:
                    break;
                case R.id.shop_classify5:
//                    intent = new Intent(mContext, ShopActivity.class);
//                    intent.putExtra("INDUSTRY_ID", 104);
//                    intent.putExtra("SHOP_NAME", "维修救援");
//                    mContext.startActivity(intent);
                    break;
            }
        }
    }
}

