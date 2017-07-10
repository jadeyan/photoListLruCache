package demo.lru.imagelist;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;

/**
 * Created by jadeyan on 2017/6/12.
 * @author jadeyan
 */

public class LinearAdapter extends RecyclerView.Adapter<LinearAdapter.SimpleViewHolder> {

    private ArrayList<String> urls;

    private Context context;

    private ImageNativeLoader instance;

    private Bitmap placeHolder;

    public LinearAdapter(Context context, ArrayList<String> data, ImageNativeLoader instance){
        urls = data;
        this.context = context;
        this.instance = instance;
        placeHolder = BitmapFactory.decodeResource(context.getResources(), R.mipmap.img_place_holder);
    }
    @Override
    public SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        View view = LayoutInflater.from(context).inflate(R.layout.item_image_list, layout);
        return new SimpleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SimpleViewHolder holder, int position) {
        holder.setImageSrc(urls.get(position));
    }

    public void onViewRecycled(SimpleViewHolder holder) {
        if(holder != null){
            Log.d(getClass().getName(), "bitmap from IV will recycled, src is " + holder.imageSrc);
            holder.photo.setImageDrawable(null);
        }
    }

    @Override
    public int getItemCount() {
        return urls == null ? 0 : urls.size();
    }

    public class SimpleViewHolder extends RecyclerView.ViewHolder {
        View rootView;

        String imageSrc;

        ImageView photo;

        public SimpleViewHolder(View itemView) {
            super(itemView);
            rootView = itemView;
            photo = (ImageView) itemView.findViewById(R.id.item_image_list_icon);
        }

        public void setImageSrc(String src) {
            imageSrc = src;
            photo.setTag(src);
            Bitmap bitmap = instance.getImageFromMemoryCache(src, photo);
            if (bitmap != null) {
                photo.setImageBitmap(bitmap);
            } else {
                photo.setImageBitmap(placeHolder);
            }
        }
    }
}
