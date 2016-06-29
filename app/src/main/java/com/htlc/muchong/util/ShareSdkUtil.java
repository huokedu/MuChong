package com.htlc.muchong.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;

import com.htlc.muchong.R;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

/**
 * Created by sks on 2016/6/21.
 */
public class ShareSdkUtil {
    /*通过sharesdk分享*/
    public static void shareByShareSDK(final Context context, final String title, final String content, final String url, final String imageUrl) {
        Picasso.with(context).load(Uri.parse(imageUrl)).into(new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                File file = new File(context.getExternalCacheDir(), imageUrl);
                share(context, title, content, url, imageUrl,file,bitmap);
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {
                File file = new File(context.getExternalCacheDir(), "ic_launcher.png");
                Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher);
                share(context, title, content, url, imageUrl,file,bitmap);
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        });

    }

    private static void share(Context context, String title, String content, String url, String imageUrl, File file, Bitmap bitmap) {
        ShareSDK.initSDK(context);
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();
        //隐藏编辑页面
        oks.setSilent(true);

        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
        oks.setTitle(title);
        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
        oks.setTitleUrl(url);
        // text是分享文本，所有平台都需要这个字段
        if(content.length()>40){
            content = content.substring(0,39);
        }
        oks.setText(content+url);
        if (!file.exists()) {
            try {
                file.createNewFile();
                FileOutputStream fos = null;
                if (!file.exists()) {
                    file.createNewFile();
                }
                fos = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        if(file.exists()){
            oks.setImagePath(file.getAbsolutePath());//确保SDcard下面存在此张图片
        }
        oks.setImageUrl(imageUrl);//确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl(url);
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment("真的不错哦！快来看看吧");
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite(context.getString(R.string.app_name));
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl(url);
        // 启动分享GUI
        oks.show(context);
    }
}
