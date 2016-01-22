package com.doyoteam.fisher.net.learn;

import com.doyoteam.fisher.db.bean.Article;
import com.doyoteam.fisher.db.bean.Page;
import com.doyoteam.fisher.db.bean.Post;
import com.doyoteam.fisher.net.HttpOut;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * 学堂响应类
 * @author guoyaobin
 * @version 1.0
 */

public class LearnHttpOut extends HttpOut {

    private ArrayList<Article> articleArrayList;
    private ArrayList<Article> recommendArrayList;
    private Page page;
    private String status;

    public String getStatus() {
        return status;
    }

    public ArrayList<Article> getArticleArrayList() {
        return articleArrayList;
    }

    public ArrayList<Article> getRecommendArrayList() {
        return recommendArrayList;
    }

    public Page getPage() {
        return page;
    }

    @Override
    public void parseData(JSONObject response) throws JSONException {
        try {
            status = response.getString("status");
            if( status.equals("SUCCESS")){
                JSONObject data = response.getJSONObject("data");
                JSONArray article_list = data.getJSONArray("article_list");
                articleArrayList = new ArrayList<Article>();
                for(int i=0; i<article_list.length(); i++)
                {
                    JSONObject jsonObject = article_list.getJSONObject(i);
                    Article article = new Article();
                    article.id = jsonObject.getString("id");
                    article.typeid = jsonObject.getString("typeid");
                    article.channel = jsonObject.getString("channel");
                    article.title = jsonObject.getString("title");
                    article.writer = jsonObject.getString("writer");
                    article.litpic = jsonObject.getString("litpic");
                    article.pubdate = jsonObject.getString("pubdate");
                    article.description = jsonObject.getString("description");
                    article.click = jsonObject.getString("click");
                    article.goodpost = jsonObject.getString("goodpost");
                    article.nid = jsonObject.getString("nid");
                    article.skin = jsonObject.getString("skin");
                    article.senddate = jsonObject.getString("senddate");
                    article.detail_url = jsonObject.getString("detail_url");
                    articleArrayList.add(article);
                }

                JSONObject page_info = data.getJSONObject("page_info");
                page = new Page();
                page.totalCount = page_info.getString("totalCount");
                page.pageCount = page_info.getString("pageCount");
                page.nextPage = page_info.getString("nextPage");
                page.perPage = page_info.getString("perPage");

//                JSONArray recommend = data.getJSONArray("recommend");
//                recommendArrayList = new ArrayList<Article>();
//                for(int i=0; i<recommend.length(); i++)
//                {
//                    JSONObject jsonObject = recommend.getJSONObject(i);
//                    Article article = new Article();
//                    article.id = jsonObject.getString("id");
//                    article.typeid = jsonObject.getString("typeid");
//                    article.channel = jsonObject.getString("channel");
//                    article.title = jsonObject.getString("title");
//                    article.writer = jsonObject.getString("writer");
//                    article.litpic = jsonObject.getString("litpic");
//                    article.pubdate = jsonObject.getString("pubdate");
//                    article.description = jsonObject.getString("description");
//                    article.click = jsonObject.getString("click");
//                    article.goodpost = jsonObject.getString("goodpost");
//                    article.format_pubdate = jsonObject.getString("format_pubdate");
//                    article.detail_url = jsonObject.getString("detail_url");
//                    recommendArrayList.add(article);
//                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
