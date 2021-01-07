package com.miss.library_glide.manager;

import androidx.fragment.app.FragmentActivity;

/**
 * 管理 RequestManager 构建出 RequestManager
 */
public class RequestManagerRetriever {
    public RequestManager get(FragmentActivity fragmentActivity) {
        return new RequestManager(fragmentActivity);
    }
}
