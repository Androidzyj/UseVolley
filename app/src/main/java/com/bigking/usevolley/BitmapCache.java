package com.bigking.usevolley;

import android.graphics.Bitmap;
import android.util.LruCache;

import com.android.volley.toolbox.ImageLoader;

/**
 * Created by Administrator on 2017/9/18.
 */

public class BitmapCache implements ImageLoader.ImageCache {
    private LruCache<String,Bitmap> mCache;


    public BitmapCache(){
        int maxSize  = 10*1024*1024;//10M
        mCache = new LruCache<String, Bitmap>(maxSize){
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getRowBytes()*value.getHeight();
            }
        };
    }

    @Override
    public Bitmap getBitmap(String url) {
        return mCache.get(url);
    }

    @Override
    public void putBitmap(String url, Bitmap bitmap) {
          mCache.put(url,bitmap);
    }
}
