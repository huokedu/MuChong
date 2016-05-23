package com.htlc.muchong.base;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.Window;
import android.widget.TextView;

import com.bugtags.library.Bugtags;
import com.htlc.muchong.R;

import core.ActionCallbackListener;

/**
 * Created by sks on 2016/5/13.
 */
public abstract class BaseActivity extends AppCompatActivity {
    public Toolbar mToolbar;
    public TextView mTitleTextView,mTitleRightTextView,mTitleLeftTextView;
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

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), getToolbarBackgroundResourcesId());
        //异步获得bitmap图片颜色值
        Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
            @Override
            public void onGenerated(Palette palette) {
                Palette.Swatch vibrant = palette.getVibrantSwatch();//有活力
//                Palette.Swatch c = palette.getDarkVibrantSwatch();//有活力 暗色
//                Palette.Swatch d = palette.getLightVibrantSwatch();//有活力 亮色
//                Palette.Swatch f = palette.getMutedSwatch();//柔和
//                Palette.Swatch a = palette.getDarkMutedSwatch();//柔和 暗色
//                Palette.Swatch b = palette.getLightMutedSwatch();//柔和 亮色
                if (vibrant != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                    int color1 = vibrant.getBodyTextColor();//内容颜色
//                    int color2 = vibrant.getTitleTextColor();//标题颜色
//                    int color3 = vibrant.getRgb();//rgb颜色
                    Window window = getWindow();
                    // 很明显，这两货是新API才有的。
                    window.setStatusBarColor(vibrant.getRgb());
                    // window.setNavigationBarColor(vibrant.getRgb());
                }
            }
        });
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
     * 基类ActionCallbackListener，用于对通用失败状态的处理，如登录过期
     *
     * @param <T>
     */
    public abstract class BaseActionCallbackListener<T> implements ActionCallbackListener<T> {

        @Override
        public abstract void onSuccess(T data);

        @Override
        public void onFailure(String errorEvent, String message) {
            onIllegalState(errorEvent, message);
        }

        public abstract void onIllegalState(String errorEvent, String message);
    }
}
