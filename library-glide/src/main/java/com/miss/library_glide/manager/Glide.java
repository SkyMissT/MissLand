package com.miss.library_glide.manager;

import android.content.Context;

import androidx.fragment.app.FragmentActivity;

public class Glide {

    private static Glide glide;

    private final RequestManagerRetriever retriever;

    private Glide(RequestManagerRetriever retriever) {
        this.retriever = retriever;
    }

    public static Glide getInstance(RequestManagerRetriever retriever) {
        if (glide == null) {
            synchronized (Glide.class) {
                if (glide == null) {
                    glide = new Glide(retriever);
                }
            }
        }
        return glide;
    }


    public static RequestManager with(FragmentActivity fragmentActivity) {
        return getRetriever(fragmentActivity).get(fragmentActivity);
    }

    private static RequestManagerRetriever getRetriever(Context context) {
        return Glide.get(context).getRetriever();
    }

    private static Glide get(Context context) {
        return new GlideBuilder(context).build();
    }

    // 下面都是具体
    public RequestManagerRetriever getRetriever() {
        return retriever;
    }

}
