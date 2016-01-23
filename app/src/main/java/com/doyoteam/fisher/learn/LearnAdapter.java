package com.doyoteam.fisher.learn;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.doyoteam.fisher.Constants;
import com.doyoteam.fisher.R;
import com.doyoteam.fisher.db.bean.Article;
import com.doyoteam.util.ImageLoaderConfig;
import com.doyoteam.util.ImageShowListener;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * LearnAdapter
 */
public class LearnAdapter extends RecyclerView.Adapter<LearnAdapter.ViewHolder> {

    private Context mContext;
    private LayoutInflater mInflater;
    private int mType;
    private List<Article> articleList;
    private List<Article> recommendList;
    private SimpleDateFormat sdf;

    public LearnAdapter(Context context, int type, List<Article> list, List<Article> rList) {
        mInflater = LayoutInflater.from(context);
        mType = type;
        articleList = list;
        recommendList = rList;
        mContext = context;
        sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
    }

    @Override
    public LearnAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView;
        if(viewType == 0)
        {
            rootView = mInflater.inflate(R.layout.item_learn_recommend, parent, false);

        }
        else
        {
            rootView = mInflater.inflate(R.layout.item_learn_article, parent, false);

        }
        return new ViewHolder(rootView, viewType);
    }

    @Override
    public void onBindViewHolder(LearnAdapter.ViewHolder holder, final int position) {
        if(position==0)
        {
            if(recommendList!=null) {
                if (recommendList.size() >= 3) {
                    holder.item_view.setVisibility(View.VISIBLE);
                    ImageLoader.getInstance().displayImage(recommendList.get(0).litpic, holder.item_iv,
                            ImageLoaderConfig.init(Constants.IMAGE_DEFAULT_POST).getDisplayImageOptions(),
                            new ImageShowListener());
                    holder.item_name1_tv.setText(recommendList.get(0).title);
                    holder.item_name1_layout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            gotoArticleDetailActivity(mType, recommendList.get(0).id);
                        }
                    });
                    holder.item_name2_tv.setText(recommendList.get(1).title);
                    holder.item_name2_layout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            gotoArticleDetailActivity(mType, recommendList.get(1).id);
                        }
                    });
                    holder.item_name3_tv.setText(recommendList.get(2).title);
                    holder.item_name3_layout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            gotoArticleDetailActivity(mType, recommendList.get(2).id);
                        }
                    });
                } else {
                        holder.item_view.setVisibility(View.GONE);
                }
            }
            else
            {
                holder.item_view.setVisibility(View.GONE);
            }
            return ;
        }

        final Article article = articleList.get(position-1);
        ImageLoader.getInstance().displayImage(article.litpic, holder.item_iv,
                ImageLoaderConfig.init(Constants.IMAGE_DEFAULT_POST).getDisplayImageOptions(),
                new ImageShowListener());
        holder.item_name.setText(article.title);
        holder.item_date.setText(sdf.format(new Date(Long.parseLong(article.pubdate))));
        holder.item_number.setText(article.click+"次点击");

        holder.item_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoArticleDetailActivity(mType,article.id);
            }
        });
    }
    private void gotoArticleDetailActivity(int type,String id)
    {
        Intent intent = null;
        if(type == Article.VIDEO)
        {
            intent = new Intent(mContext,ArticleDetailVideoActivity.class);
        }
        else
        {
            intent = new Intent(mContext,ArticleDetailActivity.class);
        }
        intent.putExtra("ARTICLE_ID",id);
//        intent.putExtra("ARTICLE_ID","11703");
        mContext.startActivity(intent);
    }

    @Override
    public int getItemCount() {
        int count = 0;
        if(articleList != null)
        {
            count = articleList.size()+1;
        }
        return count;
    }
    @Override
    public int getItemViewType(int position) {
        if(position == 0)
        {
            return 0;
        }
        else
        {
            return 1;
        }
    }
    class ViewHolder extends RecyclerView.ViewHolder {
        //recomment
        CardView item_view;
        LinearLayout item_name1_layout;
        TextView item_name1_tv;
        LinearLayout item_name2_layout;
        TextView item_name2_tv;
        LinearLayout item_name3_layout;
        TextView item_name3_tv;

        //article_list
        LinearLayout item_layout;
        ImageView item_iv;
        TextView item_name;
        TextView item_date;
        TextView item_number;

        public ViewHolder(View view, int type) {
            super(view);
            if(type == 0)
            {
                item_view = (CardView) view.findViewById(R.id.item_view);
                item_iv = (ImageView) view.findViewById(R.id.item_iv);
                item_name1_layout = (LinearLayout) view.findViewById(R.id.item_name1_layout);
                item_name1_tv = (TextView) view.findViewById(R.id.item_name1_tv);
                item_name2_layout = (LinearLayout) view.findViewById(R.id.item_name2_layout);
                item_name2_tv = (TextView) view.findViewById(R.id.item_name2_tv);
                item_name3_layout = (LinearLayout) view.findViewById(R.id.item_name3_layout);
                item_name3_tv = (TextView) view.findViewById(R.id.item_name3_tv);
                return;
            }
            item_layout = (LinearLayout) view.findViewById(R.id.item_layout);
            item_iv = (ImageView) view.findViewById(R.id.item_iv);
            item_name = (TextView) view.findViewById(R.id.item_name);
            item_date = (TextView) view.findViewById(R.id.item_date);
            item_number = (TextView) view.findViewById(R.id.item_number);
        }
    }
}
