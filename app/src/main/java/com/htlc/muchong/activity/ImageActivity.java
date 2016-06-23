package com.htlc.muchong.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.htlc.muchong.App;
import com.htlc.muchong.R;
import com.htlc.muchong.base.BaseActivity;
import com.larno.util.ToastUtil;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import model.UserBean;

/**
 * Created by sks on 2016/5/21.
 */
public class ImageActivity extends BaseActivity {
    private static final String Image_Uri = "Image_Uri";
    private ImageView imageView;

    public static void goImageActivity(Context context, String imageUri){
        Intent intent = new Intent(context,ImageActivity.class);
        intent.putExtra(Image_Uri, imageUri);
        context.startActivity(intent);
    }
    private String imageUri;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_image;
    }

    @Override
    protected void setupView() {
        imageUri = getIntent().getStringExtra(Image_Uri);

        mTitleTextView.setText(R.string.title_image);
        imageView = (ImageView)findViewById(R.id.imageView);

        initData();
    }


    @Override
    protected void initData() {
        Picasso.with(this).load(Uri.parse(imageUri)).into(imageView);
//        Picasso.with(this).load(Uri.parse(imageUri)).into(new Target() {
//            @Override
//            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
//
//            }
//
//            @Override
//            public void onBitmapFailed(Drawable errorDrawable) {
//
//            }
//
//            @Override
//            public void onPrepareLoad(Drawable placeHolderDrawable) {
//
//            }
//        });
    }
}
