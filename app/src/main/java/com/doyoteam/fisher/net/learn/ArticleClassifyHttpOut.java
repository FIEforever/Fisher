package com.doyoteam.fisher.net.learn;

import com.doyoteam.fisher.db.bean.Article;
import com.doyoteam.fisher.db.bean.Classify;
import com.doyoteam.fisher.net.HttpOut;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * 学堂文章一级下拉分类响应类
 * @author guoyaobin
 * @version 1.0
 */

public class ArticleClassifyHttpOut extends HttpOut {

    private Article article;
    private ArrayList<Classify> classifyArrayList;
    private String status;

    public String getStatus() {
        return status;
    }

    @Override
    public void parseData(JSONObject response) throws JSONException {
        try {
            status = response.getString("status");
            if( status.equals("SUCCESS")){
                JSONObject data = response.getJSONObject("data");
                if(!!data.isNull("classify"))
                {
                    JSONArray jsonArray1 = data.getJSONArray("classify");
                    classifyArrayList = new ArrayList<Classify>();
                    for(int i=0; i<jsonArray1.length(); i++)
                    {
                        JSONObject jsonObject1 = jsonArray1.getJSONObject(i);
                        Classify classify1 = new Classify();
                        classify1.id = jsonObject1.getString("typeid");
                        classify1.title = jsonObject1.getString("title");
                        if(!jsonObject1.isNull("classify_list"))
                        {
                            JSONArray jsonArray2 = jsonObject1.getJSONArray("classify_list");
                            classify1.classify_list = new ArrayList<Classify>();
                            for(int j=0;j<jsonArray2.length();j++)
                            {
                                JSONObject jsonObject2 = jsonArray2.getJSONObject(i);
                                Classify classify2 = new Classify();
                                classify2.id = jsonObject2.getString("aid");
                                classify2.title = jsonObject2.getString("title");
                                classify1.classify_list.add(classify2);
                            }
                        }
                        classifyArrayList.add(classify1);
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
