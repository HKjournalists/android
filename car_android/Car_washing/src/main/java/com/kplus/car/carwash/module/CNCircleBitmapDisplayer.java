package com.kplus.car.carwash.module;

import android.graphics.Bitmap;

import com.kplus.car.carwash.widget.CNCircleDrawable;
import com.nostra13.universalimageloader.core.assist.LoadedFrom;
import com.nostra13.universalimageloader.core.display.BitmapDisplayer;
import com.nostra13.universalimageloader.core.imageaware.ImageAware;
import com.nostra13.universalimageloader.core.imageaware.ImageViewAware;

/**
 * 圆形图片的Displayer
 * Created by Fu on 2015/6/2.
 */
public class CNCircleBitmapDisplayer implements BitmapDisplayer {
    protected final int margin;

    public CNCircleBitmapDisplayer() {
        this(0);
    }

    public CNCircleBitmapDisplayer(int margin) {
        this.margin = margin;
    }

    @Override
    public void display(Bitmap bitmap, ImageAware imageAware, LoadedFrom loadedFrom) {
        if (!(imageAware instanceof ImageViewAware)) {
            throw new IllegalArgumentException("ImageAware should wrap ImageView. ImageViewAware is expected.");
        }

        imageAware.setImageDrawable(new CNCircleDrawable(bitmap, margin));
    }
}
