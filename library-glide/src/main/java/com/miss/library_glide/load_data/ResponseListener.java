package com.miss.library_glide.load_data;


import com.miss.library_glide.resource.Value;

/**
 * 加载外部资源 成功 和 失败 回调
 */
public interface ResponseListener {

    public void responseSuccess(Value value); // 成功 Value

    public void responseException(Exception e); // 错误详情

}
