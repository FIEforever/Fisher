package com.doyoteam.fisher.learn;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.doyoteam.fisher.Constants;
import com.doyoteam.fisher.MainApp;
import com.doyoteam.fisher.R;
import com.doyoteam.fisher.db.bean.Article;
import com.doyoteam.fisher.db.bean.Image;
import com.doyoteam.util.ImageLoaderConfig;
import com.doyoteam.util.ImageShowListener;
import com.doyoteam.util.Tools;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

/**
 * Created by F on 2016/1/16.
 */
public class ArticleDetailFragment extends Fragment {

    private Context mContext;
    private LinearLayout article_text_layout;

    public static ArticleDetailFragment newInstance(Article article,ArrayList<Image> imageArrayList) {
        ArticleDetailFragment fragment = new ArticleDetailFragment();
        Bundle args = new Bundle();
        args.putSerializable("Article",article);
        args.putSerializable("ImageArrayList",imageArrayList);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_article_detail, container, false);
        initView(rootView);
        initData();
        return rootView;
    }
    private void initView(View rootView)
    {
        article_text_layout = (LinearLayout)rootView.findViewById(R.id.article_text_layout);
    }
    private void initData()
    {
        mContext = MainApp.getContext();
        Article article = (Article)getArguments().getSerializable("Article");
        ArrayList<Image> imageArrayList = (ArrayList<Image>)getArguments().getSerializable("ImageArrayList");
        setData(article,imageArrayList);
    }
    private void setData(Article article,ArrayList<Image> imageArrayList)
    {
        ArrayList<String> bodys = Tools.getFormatData(article, imageArrayList);
        for (int i=0;i<bodys.size();i++)
        {
            TextView textView = new TextView(mContext);
            textView.setTextColor(getResources().getColor(R.color.secondary_text_dark));
            textView.setTextSize(16);
            ImageView imageView = new ImageView(mContext);
            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.MATCH_PARENT,FrameLayout.LayoutParams.WRAP_CONTENT);
            imageView.setLayoutParams(layoutParams);
            imageView.setAdjustViewBounds(true);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            if(i!=(bodys.size()-1))
            {
                textView.setText(Html.fromHtml(bodys.get(i).substring(0, bodys.get(i).length() - 25)));
                ImageLoader.getInstance().displayImage(imageArrayList.get(i).url, imageView,
                        ImageLoaderConfig.init(Constants.IMAGE_DEFAULT_POST).getDisplayImageOptions(),
                        new ImageShowListener());
            }
            else
            {
                textView.setText(Html.fromHtml(bodys.get(i).substring(0,bodys.get(i).length()-4)));
            }
            article_text_layout.addView(textView);
            article_text_layout.addView(imageView);
        }
    }
}
