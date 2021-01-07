package com.miss.library_glide.manager;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import com.miss.library_glide.fragment.FragmentActivityFragmentManager;

public class RequestManager {

    private final String TAG = RequestManager.class.getSimpleName();

    //  fragment 的 tag
    private final String FRAGMENT_ACTIVITY_NAME = "Fragment_Activity_NAME";
    private final int NEXT_HANDLER_MSG = 995465; // Handler 标记  发送一次空的Handler

    private static RequestTargetEngine callback;

    private Context requestManagerContext;
    FragmentActivity fragmentActivity;

    public RequestManager(FragmentActivity fragmentActivity) {

        if (callback == null) {
            callback = new RequestTargetEngine();
        }
        this.fragmentActivity = fragmentActivity;
        requestManagerContext = fragmentActivity;

        //  开始绑定操作
        FragmentManager fragmentManager = fragmentActivity.getSupportFragmentManager();
        //  拿到 fragment
        Fragment fragment = fragmentManager.findFragmentByTag(FRAGMENT_ACTIVITY_NAME);

        //  TODO ： 此处应该有两重保障
        if (fragment == null) {

            // 如果等于null，就要去创建Fragment
            fragment = new FragmentActivityFragmentManager(callback);
            // 添加到管理器 -- fragmentManager.beginTransaction().add..
            fragmentManager.beginTransaction().add(fragment,FRAGMENT_ACTIVITY_NAME).commitNowAllowingStateLoss();

            mHandler.sendEmptyMessage(NEXT_HANDLER_MSG);
        }


    }


    final Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            /*Fragment fragment2 = fragmentActivity.getSupportFragmentManager().findFragmentByTag(FRAGMENT_ACTIVITY_NAME);
            Log.d(TAG, "Handler: fragment2" + fragment2); // 有值 ： 不在排队中，所以有值*/
            return false;
        }
    });


    public RequestTargetEngine load(String url) {
        // 空白的Handler 移除掉
        mHandler.removeMessages(NEXT_HANDLER_MSG);

        // 把值传递给 资源加载引擎
        callback.loadValueInitAction(url, requestManagerContext);

        return callback;
    }








}
