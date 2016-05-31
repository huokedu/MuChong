package com.htlc.muchong.util;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.htlc.muchong.R;
import com.htlc.muchong.widget.PickPhotoDialog;

import java.io.File;
import java.io.FileOutputStream;
import java.util.UUID;

/**
 * Created by sks on 2016/5/31.
 */
public class SelectPhotoDialogHelper {
    public static final String Path = Environment.getExternalStorageDirectory().getAbsolutePath()+"/MuChong";
    private static final int Request_Take_Photo = 4833;
    private static final int Request_Pick_Photo = 4834;
    private static final int Request_Clip_Photo = 4835;

    public interface  OnPickPhotoFinishListener{
        void onPickPhotoFinishListener(File imageFile);
    }

    private int outputX;
    private int aspectX;
    private int aspectY;
    private Activity activity;
    private PickPhotoDialog mPickPhotoDialog;
    private OnPickPhotoFinishListener listener;


    public SelectPhotoDialogHelper(Activity activity,OnPickPhotoFinishListener listener,int outputX, int aspectX,int aspectY) {
        this.activity = activity;
        this.listener = listener;
        this.outputX = outputX;
        this.aspectX = aspectX;
        this.aspectY = aspectY;
    }

    /**
     * 选择图片对话框
     */
    public void showPickPhotoDialog() {
        mPickPhotoDialog = new PickPhotoDialog(activity, R.style.TransparentAlertDialog);//创建Dialog并设置样式主题
        mPickPhotoDialog.setCanceledOnTouchOutside(true);//设置点击Dialog外部任意区域关闭Dialog
        mPickPhotoDialog.show();
        mPickPhotoDialog.textAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPickPhotoDialog != null) {
                    mPickPhotoDialog.dismiss();
                }
                pickPhotoByAlbum();

            }
        });
        mPickPhotoDialog.textCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPickPhotoDialog != null) {
                    mPickPhotoDialog.dismiss();
                }
                takePhoto();
            }
        });
        mPickPhotoDialog.textCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPickPhotoDialog != null) {
                    mPickPhotoDialog.dismiss();
                }
            }
        });

        Window win = mPickPhotoDialog.getWindow();

        WindowManager.LayoutParams params = win.getAttributes();
        win.getDecorView().setPadding(0, 0, 0, 0);
        win.getDecorView().setBackgroundColor(Color.TRANSPARENT);
        params.gravity = Gravity.BOTTOM;
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        win.setAttributes(params);

    }

    private void takePhoto() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(Path, "temp.png")));
        intent.putExtra("flag", true);
        activity.startActivityForResult(intent, Request_Take_Photo);

    }

    public void pickPhotoByAlbum() {
        Intent intent = new Intent(Intent.ACTION_PICK, null);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        activity.startActivityForResult(intent, Request_Pick_Photo);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            // 如果是直接从相册获取
            case Request_Pick_Photo:
                if (Activity.RESULT_OK != resultCode) return;
                clipPhoto(data.getData());
                break;
            // 如果是调用相机拍照时
            case Request_Take_Photo:
                if (Activity.RESULT_OK != resultCode) return;
                File file = new File(Path + "/temp.png");
                clipPhoto(Uri.fromFile(file));
                break;
            // 取得裁剪后的图片
            case Request_Clip_Photo:
                if (data != null) {
                    handleBitmapAfterClip(data);
                }
                break;
        }
    }

    /**
     * 裁剪图片方法实现
     * @param uri
     */
    public void clipPhoto(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", aspectX);
        intent.putExtra("aspectY", aspectY);
        intent.putExtra("outputX", outputX);
        int outputY = outputX * aspectY / aspectX;
        intent.putExtra("outputY", outputY);
        intent.putExtra("return-data", true);
        activity.startActivityForResult(intent, Request_Clip_Photo);
    }


    /**
     * 保存裁剪之后的图片数据
     *
     * @param intent
     */
    private void handleBitmapAfterClip(Intent intent) {
        Bundle extras = intent.getExtras();
        if (extras != null) {
            Bitmap photo = extras.getParcelable("data");
            File imageFile = saveBitmapToFile(photo, "IMG_" + UUID.randomUUID() + ".jpg");
            if(listener!=null){
                listener.onPickPhotoFinishListener(imageFile);
            }
        }
    }
    /**
     * 把图片保存到文件
     * @param bitmap
     * @param fileName
     * @return
     */
    public File saveBitmapToFile(Bitmap bitmap,String fileName) {
        File dir = new File(Path);
        if(!dir.exists()){
            dir.mkdirs();
        }
        File file = new File(dir,fileName);
        FileOutputStream fos = null;
        try {
            if(!file.exists()){
                file.createNewFile();
            }
            fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file;
    }
}
