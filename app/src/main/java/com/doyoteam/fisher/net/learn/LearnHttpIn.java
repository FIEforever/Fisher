package com.doyoteam.fisher.net.learn;

import com.doyoteam.fisher.Constants;
import com.doyoteam.fisher.net.HttpIn;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 学堂数据请求类
 * @author guoyaobin
 * @version 1.0
 */

public class LearnHttpIn extends HttpIn<LearnHttpOut> {

    private static final String METHOD_NAME = "article/index";      // 接口方法名

    public LearnHttpIn(String typeid,int page)
    {
        setBaseUrl(Constants.BASE_URL_SCHOOL);
        disableSign();
        setMethodName(METHOD_NAME);
        addData("typeid", typeid, true);
        addData("page",page,true);
    }

    @Override
    protected LearnHttpOut parseData(JSONObject response) throws JSONException {
        LearnHttpOut out = new LearnHttpOut();
        out.parseData(response);
        return out;
    }
}
