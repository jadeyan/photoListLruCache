package demo.lru.imagelist;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

public class StaggeredRecyclerActivity extends Activity {

    private RecyclerView imageList;

    private ArrayList<String> photoUrls;

    private StaggeredAdapter adapter;

    private Bitmap placeHolder;

    private ImageNativeLoader instance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staggered_recycler);

        imageList = (RecyclerView) findViewById(R.id.staggered_list);
    }

    public void onResume() {
        super.onResume();

        photoUrls = FileUtil.getDCIMPath(getContentResolver());

//        adapter = new StaggeredAdapter();
//        imageList.setAdapter(adapter);

    }
}
