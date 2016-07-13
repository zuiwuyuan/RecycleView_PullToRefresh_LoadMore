package com.lnyp.fastrecyclerview.http;

import android.content.Context;
import android.widget.Toast;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lnyp.fastrecyclerview.http.IOAuthCallBack;

/**
 * 网络请求工具类
 *
 * @author lining
 */
public class HttpUtil {


    private static HttpUtils http = new HttpUtils();

    static {

        http.configCurrentHttpCacheExpiry(1000 * 5);
        // 设置超时时间
        http.configTimeout(5 * 1000);
        http.configSoTimeout(5 * 1000);
    }

    /**
     * 发送http请求,自动实现异步处理
     *
     * @param url            请求的地址
     * @param params         请求的参数
     * @param iOAuthCallBack 数据回调接口
     */
    public static void sendRequest(final Context context,
                                   final HttpMethod method, String url, RequestParams params,
                                   final IOAuthCallBack iOAuthCallBack) {

        http.send(method, url, params, new RequestCallBack<String>() {

            @Override
            public void onStart() {

            }

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                iOAuthCallBack.getIOAuthCallBack(200, responseInfo.result);// 利用接口回调数据传输
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                Toast.makeText(context, "网络错误", Toast.LENGTH_SHORT).show();
                iOAuthCallBack.getIOAuthCallBack(400, "网络错误");// 利用接口回调数据传输
            }
        });
    }

}