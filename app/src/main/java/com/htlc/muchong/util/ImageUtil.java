package com.htlc.muchong.util;

import android.net.Uri;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

/**
 * Created by sks on 2016/6/7.
 */
public class ImageUtil {
    public static void setImageByDefault(ImageView imageView, int defaultImageId, Uri uri){
        int intrinsicWidth = imageView.getResources().getDrawable(defaultImageId).getIntrinsicWidth();
        int intrinsicHeight = imageView.getResources().getDrawable(defaultImageId).getIntrinsicHeight();
        Picasso.with(imageView.getContext()).load(uri).placeholder(defaultImageId).error(defaultImageId).resize(intrinsicWidth,intrinsicHeight).into(imageView);
    }
    public static void setCircleImageByDefault(ImageView imageView, int defaultImageId, Uri uri){
        int intrinsicWidth = imageView.getResources().getDrawable(defaultImageId).getIntrinsicWidth();
        int intrinsicHeight = imageView.getResources().getDrawable(defaultImageId).getIntrinsicHeight();
        Picasso.with(imageView.getContext()).load(uri).placeholder(defaultImageId).error(defaultImageId).resize(intrinsicWidth,intrinsicHeight).transform(new CircleTransform()).into(imageView);
    }
}
