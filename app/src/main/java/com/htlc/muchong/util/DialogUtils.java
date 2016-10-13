package com.htlc.muchong.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.htlc.muchong.R;


/**
 * Created by John_Libo on 2016/8/15.
 */
public class DialogUtils {


    /**
     * 创建列表对话框
     *
     * @param context
     *            上下文
     * @param title
     *            标题
     * @param items
     *            数据源
     * @param listener
     *            确定事件
     * @return
     */
    public static Dialog createListDialog(Context context, String title,
                                          String[] items, DialogInterface.OnClickListener listener) {
        Dialog dialog = null;
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        if(!TextUtils.isEmpty(title)){
            builder.setTitle(title);
        }
        builder.setItems(items, listener);
        builder.setPositiveButton("取消", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog = builder.create();

        WindowManager wm = ((Activity) context).getWindowManager();
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;//宽度
        int height = dm.heightPixels ;//高度
        Window w = dialog.getWindow();
        WindowManager.LayoutParams lp = w.getAttributes();
        lp.x = 0;
        final int cMakeBottom = -1000;
        lp.y = cMakeBottom;
        lp.gravity = Gravity.BOTTOM;
        lp.width = width;
        lp.height=height;
        dialog.onWindowAttributesChanged(lp);
        dialog.show();
        dialog.setCanceledOnTouchOutside(true);
        return dialog;
    }

    /**
     * 从底部滑出popwindow
     * @param context
     * @param view
     * @return
     */
    public static Dialog showDialog(Context context, View view) {

        final Dialog dlg = new Dialog(context, R.style.AnimBottomPopwindow);
        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        WindowManager wm = ((Activity) context).getWindowManager();
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;//宽度
        int height = dm.heightPixels ;//高度

        dlg.setContentView(view, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));


        Window w = dlg.getWindow();
        WindowManager.LayoutParams lp = w.getAttributes();
        lp.x = 0;
        final int cMakeBottom = -1000;
        lp.y = cMakeBottom;
        lp.gravity = Gravity.BOTTOM;
        lp.width = width;
        lp.height=height;
        dlg.onWindowAttributesChanged(lp);
        dlg.show();
        dlg.setCanceledOnTouchOutside(true);
        return dlg;
    }

}
