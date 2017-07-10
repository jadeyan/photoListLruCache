package demo.lru.imagelist;

import android.content.ContentResolver;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by jadeyan on 17/5/11.
 *
 * @author jadeyan
 */

public class FileUtil {

    public static Bitmap loadBitmapFile(String filePath, int targetScreenWidth) {
        if (TextUtils.isEmpty(filePath))
            return null;

        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, opts);
        if (opts.outWidth != -1) {
            if (targetScreenWidth == -1)
                targetScreenWidth = opts.outWidth;
            int targetH = opts.outHeight * targetScreenWidth / opts.outWidth;
            opts.outWidth = targetScreenWidth;
            opts.outHeight = targetH;
            opts.inSampleSize = 2;
            opts.inJustDecodeBounds = false;
            opts.inPreferredConfig = Bitmap.Config.RGB_565;
            return BitmapFactory.decodeFile(filePath, opts);
        } else {
            Log.d("FileUtil", "Decode bitmap failed \"" + filePath + "\"");
        }
        return null;
    }

    public static ArrayList<String> getDCIMPath(ContentResolver cr) {
        String[] projection = new String[]{
                MediaStore.Images.Media.DATA,
                MediaStore.Images.Media.BUCKET_ID,
                MediaStore.Images.Media.DISPLAY_NAME
        };

        if (cr != null) {
            Cursor cursor = cr.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection, null, null, null);

            if(cursor == null || cursor.getCount() == 0)
                return null;

            ArrayList<String> imageAbPath = new ArrayList<>();

            cursor.moveToFirst();
            do{
                String data = cursor.getString(0);
                imageAbPath.add(data);
                Log.d("FileUtil", cursor.getString(0));
                Log.d("FileUtil", cursor.getString(1));
                Log.d("FileUtil", cursor.getString(2));
            }
            while(cursor.moveToNext());
            return imageAbPath;
        }

        return null;
    }
}
