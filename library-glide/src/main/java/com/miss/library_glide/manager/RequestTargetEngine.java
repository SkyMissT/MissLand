package com.miss.library_glide.manager;

import android.content.Context;
import android.util.Log;
import android.widget.ImageView;

import com.miss.library_glide.cache.ActiveCache;
import com.miss.library_glide.cache.MemoryCache;
import com.miss.library_glide.disk.DiskLruCacheImpl;
import com.miss.library_glide.fragment.LifecycleCallback;
import com.miss.library_glide.load_data.LoadDataManager;
import com.miss.library_glide.load_data.ResponseListener;
import com.miss.library_glide.resource.Key;
import com.miss.library_glide.resource.Value;
import com.miss.library_glide.resource.ValueCallback;
import com.miss.library_glide.util.Tool;

public class RequestTargetEngine  implements LifecycleCallback, ValueCallback, ResponseListener {

    private final String TAG = RequestTargetEngine.class.getSimpleName();

    private String path;
    private Context glideContext;
    private String key; // ac037ea49e34257dc5577d1796bb137dbaddc0e42a9dff051beee8ea457a4668 (磁盘缓存用的key)
    private ImageView imageView;

    private ActiveCache activeCache; // 活动缓存
    private MemoryCache memoryCache; // 内存缓存
    private DiskLruCacheImpl diskLruCache; // 磁盘缓存

    // Glide 获取 内存的 八分之一
    private final int MEMORY_MAX_SIXE = 1024 * 1024 * 60; // 内存缓存 的 maxSize

    public RequestTargetEngine() {
        if (activeCache == null) {
            activeCache = new ActiveCache(this);
        }
        if (memoryCache == null) {
            memoryCache = new MemoryCache(MEMORY_MAX_SIXE);
        }
        diskLruCache = new DiskLruCacheImpl(); // 初始化磁盘缓存
    }


    public void loadValueInitAction(String url, Context requestManagerContext) {
        this.path = url;
        this.glideContext = requestManagerContext;
        this.key = new Key(path).getKey();
    }

    public void into(ImageView imageView) {
        this.imageView = imageView;

        Tool.checkNotEmpty(imageView); // 检测：释放是空
        Tool.assertMainThread(); // 检测：非主线程 抛出异常


        //    加载资源  ---> 缓存机制  ---> HTTP/SD/ 加载成功后  ---> 把资源保存到缓存中

        Value value = cacheAction();

        if (value != null) {
            imageView.setImageBitmap(value.getBitmap());
        }

    }

    //    加载资源  ---> 缓存机制  ---> HTTP/SD/ 加载成功后  ---> 把资源保存到缓存中
    private Value cacheAction() {

        //  第一步：判断活动缓存是否有
        Value value = activeCache.get(key);
        if (value != null) {
            Log.e(TAG, "cacheAction: 本次加载的是在（活动缓存）中获取的资源>>>");
            return value;
        }
        //  第二步：判断内存缓存是否有资源，如果有资源 剪切(内存 ---> 活动) 就返回，   否则就继续往下找
        value = memoryCache.get(key);
        if (null != value) {
            Log.e(TAG, "cacheAction: 本次加载的是在（内存缓存）中获取的资源>>>");
            // 移动操作 剪切（内存--->活动）
            activeCache.put(key, value);
            memoryCache.remove(key);
            return value;
        }

        //  第三步：从磁盘缓存中你去找，如果找到了， 把磁盘缓存的元素 加入到 活动缓存中...
        value = diskLruCache.get(key);
        if (value != null) {
            Log.e(TAG, "cacheAction: 本次加载的是在（磁盘缓存）中获取的资源>>>");

            // 把磁盘缓存中的元素 ---- 加入 ---》 活动缓存中....   不是剪切 是复制
            activeCache.put(key, value);

            return value;
        }

        //  第四步：真正去加载外部资源 数据模型LoadDat 去加载    HTTP / 本地io

        value = new LoadDataManager().loadResource(path, this, glideContext);
        if (value != null) {
            return value;
        }

        return null;
    }


    @Override
    public void glideOnStart() {
        Log.e(TAG, "glideInitAction: Glide生命周期之 已经开启了 初始化了....");
    }

    @Override
    public void glideOnStop() {
        Log.e(TAG, "glideInitAction: Glide生命周期之 已经停止中 ....");
    }

    @Override
    public void glideOnDestroy() {
        Log.d(TAG, "glideInitAction: Glide生命周期之 进行释放操作 缓存策略释放操作等  释放 活动缓存的所有资源 >>>>>> ....");

        // 活动缓存.释放操作();
        if (activeCache != null) {
            activeCache.recycleActive();  // 活动缓存 给释放掉
        }
    }

    @Override
    public void valueNonUseListener(String key, Value value) {
        // 加入到 内存缓存
        if (key != null && value != null) {
            memoryCache.put(key, value);
        }
    }


    private void saveCache(String key,Value value) {
        Log.e(TAG, "saveCache: >>>>>>>>>>>>>>>>>>>>>>>>>> 加载外置资源成功后 ，保存到缓存中， key:" + key + " value:" + value);
        value.setKey(key);

        //  存到磁盘
        if (diskLruCache != null) {
            diskLruCache.put(key, value);
        }
    }

    @Override
    public void responseSuccess(Value value) {
        if (value != null) {
            saveCache(key, value);
        }
        imageView.setImageBitmap(value.getBitmap());
    }

    @Override
    public void responseException(Exception e) {
        Log.e(TAG, "responseException: 加载外部资源失败 请检测 e:" + e.getMessage());
    }
}
