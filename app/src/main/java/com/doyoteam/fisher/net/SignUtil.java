package com.doyoteam.fisher.net;

import com.doyoteam.fisher.Constants;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * 网络接口请求封装类
 */
public class SignUtil {


    /**
     * 对参数键值对进行排序并输出经过MD5加密后字符串
     *
     * @param params 参数列表
     * @return 返回MD5密文
     */
    public static String generateSignKey(Map<String, String> params) {
        List<String> keyList = new LinkedList<>(params.keySet());
        Collections.sort(keyList);
        StringBuilder signBuf = new StringBuilder();
        for (String param : keyList) {
            signBuf.append(param).append("=").append(params.get(param)).append("&");
        }
        if (signBuf.length() > 0) {
            signBuf.deleteCharAt(signBuf.length() - 1);
        }
//		String temp = signBuf.toString();
        // 给参数字符串加上SIGNKEY
        signBuf.append(Constants.SIGN_KEY);
        // 对参数字符串进行MD5加密
//		Tools.showLog("【请求参数】：linpinSign="+signKey+"&"+temp);
        return md5Encrypt(signBuf.toString());
    }

    public static String md5Encrypt(String message) {
        String md5 = "00000000";
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(message.getBytes("UTF-8"));
            byte[] md5Bytes = md.digest();
            StringBuilder sb = new StringBuilder();
            for (byte md5Byte : md5Bytes)
                sb.append(String.format("%02x", (int) (0xff & md5Byte)));
            md5 = sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return md5;
    }
}
