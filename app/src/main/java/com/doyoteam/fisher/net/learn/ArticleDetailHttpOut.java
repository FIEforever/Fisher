package com.doyoteam.fisher.net.learn;

import com.doyoteam.fisher.db.bean.Article;
import com.doyoteam.fisher.db.bean.Image;
import com.doyoteam.fisher.db.bean.Video;
import com.doyoteam.fisher.net.HttpOut;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * 学堂文章详情响应类
 * @author guoyaobin
 * @version 1.0
 */

public class ArticleDetailHttpOut extends HttpOut {

    private Article article;
    private ArrayList<Image> imageArrayList;
    private Video video;
    private String status;

    public String getStatus() {
        return status;
    }
    public Article getArticle() {
        return article;
    }
    public ArrayList<Image> getImageArrayList() {
        return imageArrayList;
    }

    public Video getVideo() {
        return video;
    }

    @Override
    public void parseData(JSONObject response) throws JSONException {
        try {
            status = response.getString("status");
            if( status.equals("SUCCESS")){
                JSONObject data = response.getJSONObject("data");
                JSONObject jsonObject = data.getJSONObject("article");
                article = new Article();
                article.id = jsonObject.getString("id");
                article.typeid = jsonObject.getString("typeid");
                article.channel = jsonObject.getString("channel");
                article.writer = jsonObject.getString("writer");
                article.litpic = jsonObject.getString("litpic");
                article.pubdate = jsonObject.getString("pubdate");
                article.description = jsonObject.getString("description");
                article.click = jsonObject.getString("click");
                article.goodpost = jsonObject.getString("goodpost");
                article.nid = jsonObject.getString("nid");
                article.skin = jsonObject.getString("skin");
                article.body = jsonObject.getString("body");
                article.notpost = jsonObject.getString("notpost");
                article.source = jsonObject.getString("source");
                article.title = jsonObject.getString("title");

                JSONArray jsonArray = null;
                if(!data.isNull("image_list")) {
                    jsonArray = data.getJSONArray("image_list");
                    imageArrayList = new ArrayList<Image>();
                    for(int i=0; i<jsonArray.length(); i++)
                    {
                        jsonObject = jsonArray.getJSONObject(i);
                        Image image = new Image();
                        image.img_mark = jsonObject.getString("img_mark");
                        image.url = jsonObject.getString("url");
                        imageArrayList.add(image);
                    }
                }
                if(!data.isNull("video_list"))
                {
                    jsonObject = data.getJSONObject("video_list");
                    video = new Video();
                    video.name = jsonObject.getString("name");
                    video.player_type = jsonObject.getString("player_type");
                    video.url = jsonObject.getString("url");
                    video.type = jsonObject.getString("type");
                    if(!jsonObject.isNull("sub_item"))
                    {
//                            video.sub_item = jsonObject.getString("sub_item");
                    }

                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
