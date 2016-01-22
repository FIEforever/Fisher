package com.doyoteam.fisher.net.config;

import com.doyoteam.fisher.net.HttpIn;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 公共数据请求类
 * @author guoyaobin
 * @version 1.0
 */

public class ConfigHttpIn extends HttpIn<ConfigHttpOut> {

    private static final String METHOD_NAME = "json.php";      // 接口方法名

    public ConfigHttpIn()
    {
        disableSign();
        setMethodName(METHOD_NAME);
    }

    @Override
    protected ConfigHttpOut parseData(JSONObject response) throws JSONException {
        ConfigHttpOut out = new ConfigHttpOut();
        out.parseData(response);
        return out;
    }
}
