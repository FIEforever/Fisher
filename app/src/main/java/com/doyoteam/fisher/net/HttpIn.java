package com.doyoteam.fisher.net;

import com.doyoteam.fisher.Constants;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

/**
 * 网络接口请求封装类
 */
public abstract class HttpIn<T> {


    private String baseUrl;             // 接口系统根Url
    private String methodName;          // 方法名
    private RequestParams params;       // 参数
    private Map<String, String> paraArray;     // 参数集合
    private boolean needSign;

    private ActionListener<T> listener;

    /**
     * 设置网络请求结果监听器
     *
     * @param listener 监听器
     */
    public void setActionListener(ActionListener<T> listener) {
        this.listener = listener;
    }

    /**
     * 请求成功数据解析器
     *
     * @param response 返回成功数据
     * @return 数据对象
     * @throws JSONException
     */
    protected abstract T parseData(JSONObject response) throws JSONException;

    public HttpIn() {
        this.params = new RequestParams();
        this.paraArray = new HashMap<String, String>();
        this.baseUrl = Constants.BASE_URL;
        this.needSign = true;               // 默认接口访问需要签名
    }

    // 设置接口无需签名
    public void disableSign() {
        this.needSign = false;
    }

    // 获取接口是否需要签名
    public boolean needSign() {
        return needSign;
    }

    /**
     * 设置接口地址
     *
     * @param baseUrl 接口方法名
     */
    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    /**
     * 设置接口方法名
     *
     * @param methodName 接口方法名
     */
    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    /**
     * 获取接口方法名
     *
     * @return 接口方法名
     */
    public String getMethodName() {
        return this.methodName;
    }

    /**
     * 添加接口方法参数
     *
     * @param key    参数名
     * @param value  参数值
     * @param isMust 是否必填参数
     */
    public void addData(String key, Object value, boolean isMust) {
        params.put(key, value);
        if (isMust) paraArray.put(key, value.toString());
    }

    /**
     * 删除接口方法参数
     *
     * @param key    参数名
     * @param isMust 是否必填参数
     */
    public void removeData(String key, boolean isMust) {
        params.remove(key);
        if (isMust) paraArray.remove(key);
    }

    /**
     * 添加带文件流的参数
     *
     * @param key    参数名
     * @param file   文件
     * @param isMust 是否必填参数
     */
    public void addData(String key, File file, boolean isMust) {
        try {
            params.put(key, file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        if (isMust) paraArray.put(key, file.getPath());
    }

    /**
     * 获取接口请求URL
     *
     * @return URL
     */
    public String getUrl() {
        return baseUrl + methodName;
    }

    /**
     * 获取接口参数集
     *
     * @return 参数集
     */
    public RequestParams getParam() {
        if (needSign) {
            addData(Constants.TOKEN_NAME, Constants.TOKEN_KEY, true);
            params.put(Constants.SIGN_NAME, SignUtil.generateSignKey(paraArray));
//            Tools.showToast(Constants.SIGN_NAME+":"+SignUtil.generateSignKey(paraArray));
        }
        return params;
    }

    /**
     * 获取请求完整的URL（签名以后）
     * @return 完整URL
     */
    public String getFullUrl() {
        return getUrl() + "?" + getParam().toString();
    }

    /**
     * 调用成功反馈函数
     *
     * @param response 请求数据
     * @throws JSONException
     */
    public void onSuccess(JSONObject response) throws JSONException {
        T result = parseData(response);
        if (listener != null) {
            listener.onSuccess(result);
        }
    }

    /**
     * 调用失败反馈函数
     *
     * @param statusCode 错误原因
     */
    public void onFailure(String statusCode) {
        if (listener != null) {
            listener.onFailure(statusCode);
        }
    }

    public interface ActionListener<T> {
        /**
         * 成功 ，result != null,则有数据，否则返回空值
         */
        void onSuccess(T result);

        /**
         * 错误 ，父类有 Toast 提示,如不需要提示，则子类复写该方法
         */
        void onFailure(String errInfo);
    }
}
