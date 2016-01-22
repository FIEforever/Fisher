package com.doyoteam.fisher.net.config;

import com.doyoteam.fisher.db.bean.Post;
import com.doyoteam.fisher.net.HttpOut;
import com.doyoteam.util.Tools;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * 公共数据响应类
 * @author guoyaobin
 * @version 1.0
 */

public class ConfigHttpOut extends HttpOut {

    private String status;
    private ArrayList<Post> indexArrayList;
    private ArrayList<Post> forumArrayList;

    public String getStatus() {
        return status;
    }

    public ArrayList<Post> getIndexArrayList() {
        return indexArrayList;
    }

    public ArrayList<Post> getForumArrayList() {
        return forumArrayList;
    }

    @Override
    public void parseData(JSONObject response) throws JSONException {
        try {
            status = response.getString("status");
            if( status.equals("SUCCESS")){
                JSONObject data = response.getJSONObject("data");
                JSONArray index_menu = data.getJSONObject("index").getJSONArray("menu");
                indexArrayList = new ArrayList<Post>();
                for(int i=0; i<index_menu.length(); i++)
                {
                    JSONObject forum = index_menu.getJSONObject(i);
                    Post post = new Post();
                    post.forum_id = forum.getString("forum_id");
                    post.name = forum.getString("name");
                    post.target = forum.getString("target");
                    post.type = forum.getString("type");
                    post.allow_post = forum.getString("allow_post");
//                    post.desc = forum.getString("desc");
                    post.icon = forum.getString("icon");
                    indexArrayList.add(post);
                }
                JSONArray forum_menu = data.getJSONObject("forum").getJSONArray("menu");
                forumArrayList = new ArrayList<Post>();
                for(int i=0; i<forum_menu.length(); i++)
                {
                    JSONObject forum = forum_menu.getJSONObject(i);
                    Post post = new Post();
                    post.forum_id = forum.getString("forum_id");
                    post.name = forum.getString("name");
                    post.target = forum.getString("target");
                    post.type = forum.getString("type");
                    post.allow_post = forum.getString("allow_post");
                    post.desc = forum.getString("desc");
                    post.icon = forum.getString("icon");
                    forumArrayList.add(post);
                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
