package com.doyoteam.fisher.learn;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.doyoteam.fisher.R;
import com.doyoteam.fisher.db.bean.Article;
import com.doyoteam.fisher.db.bean.Image;
import com.doyoteam.fisher.net.HttpClient;
import com.doyoteam.fisher.net.HttpIn;
import com.doyoteam.fisher.net.learn.ArticleDetailHttpIn;
import com.doyoteam.fisher.net.learn.ArticleDetailHttpOut;
import com.doyoteam.util.SlideBackAppCompatActivity;
import com.doyoteam.util.Tools;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * 文章详情
 * Created by F on 2016/1/9.
 */
public class ArticleDetailActivity extends SlideBackAppCompatActivity implements View.OnClickListener{
    //界面
    private TextView title;
    private ImageView back;
    private TextView article_title;
    private TextView article_source;
    private TextView article_time;
    private ArticleDetailFragment article_detail_fragment;
    private LinearLayout article_like_layout;
    //数据
    private SimpleDateFormat sdf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initData();
    }
    private void initData()
    {
        sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        String articleId = getIntent().getStringExtra("ARTICLE_ID");
        getArticleDetailNet(articleId);

    }
    private void initView()
    {
        setContentView(R.layout.acitivity_article_detail);
        back = ((ImageView) findViewById(R.id.title_bar_return));
        title = ((TextView) findViewById(R.id.title_bar_title));
        article_title = ((TextView) findViewById(R.id.article_title));
        article_source = ((TextView) findViewById(R.id.article_source));
        article_time = ((TextView) findViewById(R.id.article_time));
//        article_detail_fragment = (getFragmentManager())findViewById(R.id.article_detail_fragment);
        article_like_layout = (LinearLayout) findViewById(R.id.article_like_layout);
        back.setOnClickListener(this);
        title.setText(getString(R.string.learn_artcle_detail));
    }
    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            default:
                break;
            case R.id.title_bar_return:
                finish();
                break;
        }
    }

    private void getArticleDetailNet(String articleId)
    {
        ArticleDetailHttpIn request = new ArticleDetailHttpIn(articleId);
        request.setActionListener(new HttpIn.ActionListener<ArticleDetailHttpOut>() {
            @Override
            public void onSuccess(ArticleDetailHttpOut result) {
                String status = result.getStatus();
                if (status.equals("SUCCESS")) {
                    setArticleDetailData(result.getArticle(),result.getImageArrayList());
                } else {
                    Tools.showToast(status);
                }
            }

            @Override
            public void onFailure(String errInfo) {
                Tools.showToast(errInfo);
            }
        });
        HttpClient.get(request);
    }
    private void setArticleDetailData(Article article,ArrayList<Image> imageArrayList)
    {
        article_title.setText(article.title);
        article_source.setText("来源:" + article.source);
        article_time.setVisibility(View.VISIBLE);
        article_like_layout.setVisibility(View.VISIBLE);
        article_time.setText(sdf.format(new Date(Long.parseLong(article.pubdate))));
        //图文混排
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.article_text_layout, ArticleDetailFragment.newInstance(article, imageArrayList));
        fragmentTransaction.commit();
    }
}