package com.doyoteam.fisher.learn;

import android.content.Context;
import android.content.Intent;
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
    private SimpleDateFormat sdf;

    public LearnAdapter(Context context, int type, List<Article> list) {
        mInflater = LayoutInflater.from(context);
        mType = type;
        articleList = list;
        mContext = context;
        sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
    }

    @Override
    public LearnAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView;
        rootView = mInflater.inflate(R.layout.item_learn_article, parent, false);
        return new ViewHolder(rootView, mType);
    }

    @Override
    public void onBindViewHolder(LearnAdapter.ViewHolder holder, final int position) {
        final Article article = articleList.get(position);
        ImageLoader.getInstance().displayImage(article.litpic, holder.item_iv,
                ImageLoaderConfig.init(Constants.IMAGE_DEFAULT_POST).getDisplayImageOptions(),
                new ImageShowListener());
        holder.item_name.setText(article.title);
        holder.item_date.setText(sdf.format(new Date(Long.parseLong(article.pubdate))));
        holder.item_number.setText(article.click+"次点击");

        holder.item_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = null;
                switch (mType)
                {
                    case Article.VIDEO:
                        intent = new Intent(mContext,ArticleDetailVideoActivity.class);
//                        intent.putExtra("ARTICLE_ID",articleList.get(position).id);
                        intent.putExtra("ARTICLE_ID","11703");
//                        intent = new Intent(mContext,WebViewActivity.class);
//                        intent.putExtra("action_name","视频详情");
//                        intent.putExtra("action_url","http://www.iqiyi.com/common/openiframe.html?videoid=82f5fd3b6460743c4b6017a03d0d3a15&tvid=4008977309&flashvars=autoPlay%3D1%26bd%3D1%26coop%3Dcoop_baidump3%26cid%3Dqc_100001_300089");
//                        intent.putExtra("action_url","http://v.ku6.com/show/1dQBFwUpC9a9sf8q0lMpIw...html?st=1_9_2_1&nr=1");
//                        intent.putExtra("action_url",article.detail_url);
                        mContext.startActivity(intent);
                        break;
                    default:
                        intent = new Intent(mContext,ArticleDetailActivity.class);
                        intent.putExtra("ARTICLE_ID",articleList.get(position).id);
                        mContext.startActivity(intent);
                        break;
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return articleList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout item_layout;
        ImageView item_iv;
        TextView item_name;
        TextView item_date;
        TextView item_number;

        public ViewHolder(View view, int type) {
            super(view);
            item_layout = (LinearLayout) view.findViewById(R.id.item_layout);
            item_iv = (ImageView) view.findViewById(R.id.item_iv);
            item_name = (TextView) view.findViewById(R.id.item_name);
            item_date = (TextView) view.findViewById(R.id.item_date);
            item_number = (TextView) view.findViewById(R.id.item_number);
        }
    }
}
