package com.miss.library_glide.cache;

import android.os.Build;
import android.util.LruCache;

import com.miss.library_glide.resource.Value;

public class MemoryCache extends LruCache<String, Value> {


    public MemoryCache(int maxSize) {
        super(maxSize);
    }


    @Override
    protected int sizeOf(String key, Value value) {



        int sdkInt = Build.VERSION.SDK_INT;
        if (sdkInt >= Build.VERSION_CODES.KITKAT) { // 4.4
            return value.getBitmap().getAllocationByteCount();
        }

        return value.getBitmap().getByteCount();
    }
}
