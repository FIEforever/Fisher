package com.doyoteam.fisher.net;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 网络接口请求封装类
 */
public abstract class HttpOut {
	public abstract void parseData(JSONObject response) throws JSONException ;
}