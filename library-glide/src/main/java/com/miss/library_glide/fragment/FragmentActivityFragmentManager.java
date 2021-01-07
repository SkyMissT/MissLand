package com.miss.library_glide.fragment;

import androidx.fragment.app.Fragment;

/**
 * FragmentActivity 生命周期 关联 管理   空白的Fragment
 */
public class FragmentActivityFragmentManager extends Fragment {

    private LifecycleCallback callback; // 回调接口

    public FragmentActivityFragmentManager(LifecycleCallback callback) {
        this.callback = callback;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (callback != null) {
            callback.glideOnStart();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (null != callback) {
            callback.glideOnStop();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (null != callback) {
            callback.glideOnDestroy();
        }
    }
}
