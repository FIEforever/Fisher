package com.doyoteam.fisher.net.learn;

import com.doyoteam.fisher.Constants;
import com.doyoteam.fisher.net.HttpIn;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 学堂文章一级下拉分类请求类
 * @author guoyaobin
 * @version 1.0
 */

public class ArticleClassifyHttpIn extends HttpIn<ArticleDetailHttpOut> {

    private static final String METHOD_NAME = "article/classify";      // 接口方法名

    public ArticleClassifyHttpIn(String article_id)
    {
        setBaseUrl(Constants.BASE_URL_SCHOOL);
        disableSign();
        setMethodName(METHOD_NAME);
        addData("article_id", article_id, true);
    }

    @Override
    protected ArticleDetailHttpOut parseData(JSONObject response) throws JSONException {
        ArticleDetailHttpOut out = new ArticleDetailHttpOut();
        out.parseData(response);
        return out;
    }
}
