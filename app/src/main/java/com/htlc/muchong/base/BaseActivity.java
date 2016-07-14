package com.htlc.muchong.base;

import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.bugtags.library.Bugtags;
import com.htlc.muchong.App;
import com.htlc.muchong.R;
import com.htlc.muchong.activity.LoginActivity;
import com.htlc.muchong.util.LoginUtil;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import java.util.List;

import core.ActionCallbackListener;

/**
 * Created by sks on 2016/5/13.
 * 所有Activity的父类
 */
public abstract class BaseActivity extends AppCompatActivity {
    public static final String ActivityTitleId = "ActivityTitleId";
    public static final String ActivityTitle = "ActivityTitle";
    public static final String ERROR_TOKEN_TIMEOUT = "token失效";
    public static final String ERROR_BOUND_NO_ENOUGH = "保证金不足";
    public static final String ERROR_NO_ENOUGH_DATA = "没有更多数据";
    public Toolbar mToolbar;
    public TextView mTitleTextView, mTitleRightTextView, mTitleLeftTextView;

    @Override
    protected void onResume() {
        super.onResume();
        Bugtags.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Bugtags.onPause(this);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        Bugtags.onDispatchTouchEvent(this, event);
        return super.dispatchTouchEvent(event);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle("");
        mToolbar.setNavigationIcon(R.mipmap.ic_back);
        mToolbar.setBackgroundResource(getToolbarBackgroundResourcesId());
        mTitleTextView = (TextView) mToolbar.findViewById(R.id.toolbar_title);
        mTitleRightTextView = (TextView) mToolbar.findViewById(R.id.title_right);
        mTitleLeftTextView = (TextView) mToolbar.findViewById(R.id.title_left);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setStatusBarColor();
        setupView();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * @return
     */
    protected int getToolbarBackgroundResourcesId() {
        return R.mipmap.bg_toolbar;
    }

    /**
     * 设置状态栏颜色
     */
    protected void setStatusBarColor() {
        // 修改状态栏颜色，4.4+生效
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus();
        }
        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setStatusBarTintResource(R.mipmap.bg_toolbar);//通知栏所需颜色
    }

    /**
     * 设置状态栏颜色
     */
    protected void setStatusBarColor(int imageId) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT)
            return;
//        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), imageId);
//        //异步获得bitmap图片颜色值
//        Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
//            @Override
//            public void onGenerated(Palette palette) {
//                Palette.Swatch vibrant = palette.getVibrantSwatch();//有活力
//                Palette.Swatch vibrant1 = palette.getDarkVibrantSwatch();//有活力 暗色
//                Palette.Swatch vibrant2 = palette.getLightVibrantSwatch();//有活力 亮色
//                Palette.Swatch vibrant3 = palette.getMutedSwatch();//柔和
//                Palette.Swatch vibrant4 = palette.getDarkMutedSwatch();//柔和 暗色
//                Palette.Swatch vibrant5 = palette.getLightMutedSwatch();//柔和 亮色
//                if (vibrant != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//                    int color1 = vibrant.getBodyTextColor();//内容颜色
//                    int color2 = vibrant.getTitleTextColor();//标题颜色
//                    int color3 = vibrant.getRgb();//rgb颜色
//                    setTranslucentStatus();
//                    SystemBarTintManager tintManager = new SystemBarTintManager(BaseActivity.this);
//                    tintManager.setStatusBarTintEnabled(true);
//                    tintManager.setStatusBarTintColor(0xd58d6f4);//通知栏所需颜色
//                }
//
//            }
//        });

        setTranslucentStatus();
        SystemBarTintManager tintManager = new SystemBarTintManager(BaseActivity.this);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setStatusBarTintResource(imageId);//通知栏所需颜色
        tintManager.setStatusBarTintResource(android.R.color.background_dark);//通知栏所需颜色
//        tintManager.setStatusBarTintResource(R.color.statusBar);//通知栏所需颜色
    }

    @TargetApi(19)
    protected void setTranslucentStatus() {
        Window window = getWindow();
        window.setFlags(
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
    }

    /**
     * 设置布局文件id
     *
     * @return
     */
    protected abstract int getLayoutId();

    /**
     * 初始化view
     */
    protected abstract void setupView();

    /**
     * 初始化数据
     */
    protected abstract void initData();

    /**
     * 判断是否显示 没有数据的图片
     * @param list
     */
    protected void showOrHiddenNoDataView(List list, View noDataView){
        if(list==null || list.size()<=0){
            noDataView.setVisibility(View.VISIBLE);
        }else {
            noDataView.setVisibility(View.GONE);
        }
    }

    /**
     * 基类ActionCallbackListener，用于对通用失败状态的处理，如登录过期
     *
     * @param <T>
     */
    public abstract class BaseActionCallbackListener<T> implements ActionCallbackListener<T> {

        @Override
        public abstract void onSuccess(T data);

        @Override
        public void onFailure(String errorEvent, String message) {
            if(ERROR_TOKEN_TIMEOUT.equals(message)){
                showTips();
                return;
            }
            onIllegalState(errorEvent, message);
        }

        public abstract void onIllegalState(String errorEvent, String message);
    }

    private void showTips() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this,R.style.DialogAppCompat);
        builder.setCancelable(true);
        View view = View.inflate(this, R.layout.dialog_layout, null);
        builder.setView(view);
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
        alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                LoginUtil.clearUser();
                App.app.setIsLogin(false);
            }
        });

        TextView textMessage = (TextView) view.findViewById(R.id.textMessage);
        textMessage.setText(R.string.login_timeout_tips);
        view.findViewById(R.id.positiveButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(alertDialog!=null){
                    alertDialog.dismiss();
                }
                LoginUtil.clearUser();
                App.app.setIsLogin(false);
                startActivity(new Intent(BaseActivity.this, LoginActivity.class));
            }
        });
        view.findViewById(R.id.negativeButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (alertDialog != null) {
                    alertDialog.dismiss();
                }
                LoginUtil.clearUser();
                App.app.setIsLogin(false);
            }
        });
    }
}
