package com.doyoteam.fisher.net;

import com.doyoteam.util.Tools;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * 网络访问处理类
 */
public class HttpClient {

    private static AsyncHttpClient client = new AsyncHttpClient();

    public static void get(final HttpIn<?> in) {

        client.get(in.getUrl(), in.getParam(), new AsyncHttpResponseHandler() {

            @Override
            public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {

                if (arg3 instanceof IOException) {
                    in.onFailure("无法连接服务器，请检查网络");
                } else in.onFailure(String.valueOf(arg0));
            }

            @Override
            public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
                try {
                    JSONObject json = new JSONObject(new String(arg2));
                    if (in.needSign()) {
                        String status = json.optString("status", "");
                        if (status.equals("SUCCESS")) {
                            in.onSuccess(json);
                            return;
                        }
                        String resMsg = json.optString("message");
                        if (resMsg == null) {
                            resMsg = "未知错误";
                        }
                        in.onFailure(resMsg);
                    } else {
                        in.onSuccess(json);
                    }
                } catch (JSONException e) {
                    in.onFailure("数据解析错误");
                    e.printStackTrace();
                }
            }
        });
    }

    public static void post(final HttpIn<?> in) {

        Tools.showLog(in.getUrl() + "?" + in.getParam());
        client.post(in.getUrl(), in.getParam(), new AsyncHttpResponseHandler() {

            @Override
            public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
                arg3.printStackTrace();
                if (arg3 instanceof IOException) {
                    in.onFailure("无法连接服务器，请检查网络");
                } else in.onFailure(String.valueOf(arg0));
            }

            @Override
            public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
                try {
                    String response = new String(arg2);
                    Tools.showLog(response);

                    JSONObject json = new JSONObject(response);
                    if (in.needSign()) {
                        String status = json.optString("status", "ERROR");
                        String resMsg = json.optString("message");
                        if (status.equals("SUCCESS")) {
                            in.onSuccess(json);
                            return;
                        }
                        if (resMsg == null) {
                            resMsg = "未知错误";
                        }
                        in.onFailure(resMsg);
                    } else {
                        in.onSuccess(json);
                    }
                } catch (JSONException e) {
                    in.onFailure("数据解析错误");
                    e.printStackTrace();
                }
            }
        });
    }
}
