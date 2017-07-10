package demo.lru.imagelist;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import java.util.ArrayList;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class MainActivity extends AppCompatActivity implements BitmapLoadCallback{

    private RecyclerView imageList;

    private Handler handler;

    ImageNativeLoader instance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        imageList = (RecyclerView) findViewById(R.id.image_list);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        imageList.setLayoutManager(layoutManager);

        imageList.addOnScrollListener(scrollListener);

        WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);

        Point outSize = new Point();
        wm.getDefaultDisplay().getSize(outSize);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);

        int screenW = (int) (outSize.x / outMetrics.density);

        handler = new Handler();
        instance = ImageNativeLoader.getInstance(screenW, MainActivity.this);

    }

    public void onResume() {
        super.onResume();

        ArrayList<String> imageDatas = FileUtil.getDCIMPath(getContentResolver());

        LinearAdapter adapter = new LinearAdapter(this, imageDatas, instance);
        imageList.setAdapter(adapter);

    }

    private long oldMills;

    private boolean stopLoadingFlag;

    private RecyclerView.OnScrollListener scrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            stopLoadingFlag = newState == RecyclerView.SCROLL_STATE_SETTLING;
            instance.setStopLoading(stopLoadingFlag);
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            long curMills = System.currentTimeMillis();
            if(oldMills == 0)
                oldMills = curMills;

            oldMills = curMills;

            if(dy < 70 && stopLoadingFlag) {
                stopLoadingFlag = false;
                instance.setStopLoading(false);
            }
        }
    };

    @Override
    public void onLoadFinish(String key, final Bitmap bitmap, final View view) {
        if(bitmap != null && view != null && view.getTag().equals(key)){
            handler.post(new Runnable() {
                @Override
                public void run() {
                    if(view instanceof ImageView){
                        ImageView imageView = (ImageView)view;
                        imageView.setImageBitmap(bitmap);
                    }
                }
            });
        }
    }
}
