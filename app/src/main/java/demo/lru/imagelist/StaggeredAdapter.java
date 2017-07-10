package demo.lru.imagelist;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * Created by jadeyan on 2017/6/12.
 */

public class StaggeredAdapter extends RecyclerView.Adapter<StaggeredAdapter.ViewHolder> {

    private ArrayList<String> photoUrls;

    private Context context;

    private ImageNativeLoader instance;

    private Bitmap placeHolder;

    public StaggeredAdapter(Context context, ArrayList<String> photoUrls, ImageNativeLoader instance) {
        this.context = context;
        this.photoUrls = photoUrls;
        this.instance = instance;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
