package demo.lru.imagelist;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.util.Log;
import android.util.LruCache;
import android.widget.ImageView;

import java.io.File;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by jadeyan on 17/5/11.
 * @author jadeyan
 */

public class ImageNativeLoader {

    private ExecutorService cachedThreadPool = Executors.newFixedThreadPool(3);

    private ImageLruCache imageCaches;

    private static ImageNativeLoader instance;

    private int targetScreenWidth = -1;

    private BitmapLoadCallback callback;

    private boolean stopLoading;

    private ImageNativeLoader(int tartScreenWidth, BitmapLoadCallback callback) {
        int maxSize = (int) Runtime.getRuntime().maxMemory() / 2;
        Log.d(getClass().getName(), "max memory size is " + String.valueOf(maxSize/(1024*1024)) + "M");
        imageCaches = new ImageLruCache(maxSize);
        targetScreenWidth = tartScreenWidth;
        this.callback = callback;
    }

    public static ImageNativeLoader getInstance(int targetScreenWidth, BitmapLoadCallback callback){
        if(instance == null){
            synchronized (ImageNativeLoader.class){
                if(instance == null)
                    instance = new ImageNativeLoader(targetScreenWidth, callback);
            }
        }
        return instance;
    }

    public void setStopLoading(boolean stop){
        stopLoading = stop;
    }

    public class ImageLruCache extends LruCache<String, Bitmap> {
        /**
         * @param maxSize for caches that do not override {@link #sizeOf}, this is
         *                the maximum number of entries in the cache. For all other caches,
         *                this is the maximum sum of the sizes of the entries in this cache.
         */
        public ImageLruCache(int maxSize) {
            super(maxSize);
        }

        protected void entryRemoved(boolean evicted, String key, Bitmap oldValue, Bitmap newValue) {
            if (evicted && newValue == null) {
                Log.d(getClass().getName(), "bitmap recycled \"" + key + "\" from memory");
                if(!oldValue.isRecycled())
                    oldValue.recycle();
            }
        }

        /**
         * This method is very important
         * @param key
         * @param value
         * @return
         */
        protected int sizeOf(String key, Bitmap value) {
            Log.d(getClass().getName(),"image:" + key+ "  size:" + value.getByteCount()/1024 + "K");
            return value.getByteCount();
        }
    }

    private void loadImageToMemoryCache(final String key, final WeakReference<ImageView> photoRef) {
        if(!stopLoading)
            cachedThreadPool.execute(new Runnable() {
                @Override
                public void run() {
                    String tempKey = key;
                    if (key != null && key.indexOf(File.separatorChar) > 0) {
                        tempKey = key.substring(key.lastIndexOf(File.separatorChar));
                    }
                    Bitmap bitmap = FileUtil.loadBitmapFile(key, targetScreenWidth);
                    if (bitmap != null) {
                        imageCaches.put(key, bitmap);
                        ImageView imageView = photoRef.get();
                        if(imageView != null && callback != null)
                            callback.onLoadFinish(key, bitmap, imageView);
                        Log.d(getClass().getName(), "loaded bitmap \"" + tempKey + "\" from local");
                    }
                }
            });
    }

    public Bitmap getImageFromMemoryCache(String key, ImageView photo) {
        if (TextUtils.isEmpty(key))
            return null;

        Bitmap cachedBitmap = imageCaches.get(key);
        if (cachedBitmap == null) {
                WeakReference<ImageView> photoRef = new WeakReference<ImageView>(photo);
                loadImageToMemoryCache(key, photoRef);
        } else {
            if (key.indexOf(File.separatorChar) > 0)
                key = key.substring(key.lastIndexOf(File.separatorChar));
            Log.d(getClass().getName(), "get bitmap \"" + key + "\" from memory cache");
        }
        return cachedBitmap;
    }
}
