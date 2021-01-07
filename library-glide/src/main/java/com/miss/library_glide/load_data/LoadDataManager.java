package com.miss.library_glide.load_data;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.miss.library_glide.resource.Value;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class LoadDataManager implements ILoadData,Runnable{

    private final static String TAG = LoadDataManager.class.getSimpleName();

    private String path;
    private ResponseListener responseListener;
    private Context context;

    @Override
    public Value loadResource(String path, ResponseListener responseListener, Context context) {

        this.path = path;
        this.responseListener = responseListener;
        this.context = context;

        // 加载   网络图片   本地存储图片  ......
        Uri uri = Uri.parse(path);


        // 网络图片
        if ("HTTP".equalsIgnoreCase(uri.getScheme()) || "HTTPS".equalsIgnoreCase(uri.getScheme())) {
            // 线程池
            // 异步
            new ThreadPoolExecutor(0,
                    Integer.MAX_VALUE,
                    60, TimeUnit.SECONDS,
                    new SynchronousQueue<Runnable>()).execute(this); // 一执行后   run 函数执行 异步操作
        }

        // SD本地资源 返回Value，我本地资源，不需要异步线程，所以我可以自己返回

        // ....

        return null;
    }

    @Override
    public void run() {

        InputStream inputStream =  null;  // 成果
        HttpURLConnection httpURLConnection = null; // HttpURLConnection内部已经是Okhttp，因为太高效了

        try {
            URL url = new URL(path);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setConnectTimeout(5000);

            final int responseCode = httpURLConnection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) { // 200
                inputStream = httpURLConnection.getInputStream();
                final Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

                // TODO 同学们注意：我们没有写这个代码： Bitmap 做缩放，做比例，做压缩，.....  是为了学缓存

                // 成功 切换主线程  Ui线程 == Looper.getMainLooper()
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        Value value = new Value();
                        value.setBitmap(bitmap);

                        // 回调成功
                        responseListener.responseSuccess(value);
                    }
                });
            } else {
                // 失败 切换主线程
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        // 回调失败
                        responseListener.responseException(new IllegalStateException("请求失败，请求码：" + responseCode));
                    }
                });
            }
        }catch (Exception e) {
            e.printStackTrace();
        } finally {  // 程序员的 规则 不然会被鄙视的
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.d(TAG, "run: 关闭 inputStream.close(); e:" + e.getMessage());
                }
            }

            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
        }
    }



}
