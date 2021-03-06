package cn.finalteam.rxgalleryfinal.imageloader;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

/**
 * Desction:
 * Author:pengjianbo
 * Date:16/6/17 下午1:05
 */
public interface AbsImageLoader {
    void displayImage(Object context, String path,
                      ImageView imageView, Drawable defaultDrawable, Bitmap.Config config,
                      boolean resize, int width, int height, int rotate);
}
