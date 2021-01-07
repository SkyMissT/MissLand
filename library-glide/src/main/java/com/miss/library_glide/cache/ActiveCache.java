package com.miss.library_glide.cache;

import com.miss.library_glide.resource.Value;
import com.miss.library_glide.resource.ValueCallback;
import com.miss.library_glide.util.Tool;

import java.util.HashMap;
import java.util.Map;

public class ActiveCache {

    private Map<String, Value> mapList = new HashMap<>();

    private ValueCallback valueCallback;

    public ActiveCache(ValueCallback valueCallback) {
        this.valueCallback = valueCallback;
    }


    /**
     *      添加活动缓存
     * @param key
     * @param value
     */
    public void put(String key,Value value) {
        Tool.checkNotEmpty(key);
        value.setCallback(valueCallback);
        mapList.put(key, value);
    }


    /**
     *      给外界获取 value
     * @param key
     * @return
     */
    public Value get(String key) {
        Value value = mapList.get(key);
        if (value != null) {
            return value;
        }
        return null;
    }

    /**
     *      释放活动资源
     */
    public void recycleActive() {
        for (Map.Entry<String, Value> entry : mapList.entrySet()) {
            entry.getValue().recycle();
            mapList.remove(entry.getKey());
        }
    }

}
