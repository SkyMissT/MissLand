package com.miss.library_glide.manager;

import android.content.Context;

/**
 * 有很多参数
 * 无限次的添加
 * ....
 *
 * Build --->  结果
 */
public class GlideBuilder {

    public GlideBuilder(Context context) {}

    // 创建Glide
    public Glide build() {
        RequestManagerRetriever requestManagerRetriever = new RequestManagerRetriever();
        Glide glide = Glide.getInstance(requestManagerRetriever); // 实例化 Glide 仅此而已
        return glide;
    }
}
