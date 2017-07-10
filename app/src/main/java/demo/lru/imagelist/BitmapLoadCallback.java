package demo.lru.imagelist;

import android.graphics.Bitmap;
import android.view.View;

/**
 * Created by jadeyan on 17/5/18.
 */

public interface BitmapLoadCallback {

    public void onLoadFinish(String key, Bitmap bitmap, View view);
}
