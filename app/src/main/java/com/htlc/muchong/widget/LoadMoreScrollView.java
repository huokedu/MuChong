package com.htlc.muchong.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.ScrollView;

/**
 * Created by sks on 2016/6/7.
 */
public class LoadMoreScrollView extends ScrollView {
    public interface OnScrollChangedListener {
        void onScrollChanged(int l, int t, int oldl, int oldt);

        void onScrollTop();

        void onScrollBottom();

    }
    // 滚动监听者
    private OnScrollChangedListener onScrollChangedListener;

    public LoadMoreScrollView(Context context) {
        super(context);
    }

    public LoadMoreScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LoadMoreScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public LoadMoreScrollView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }


    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);

        if (null == onScrollChangedListener) {
            return;
        }
        onScrollChangedListener.onScrollChanged(l, t, oldl, oldt);
        if ((this.getHeight() + oldt) != this.getTotalVerticalScrollRange()
                && (this.getHeight() + t) == this.getTotalVerticalScrollRange()) {
            onScrollChangedListener.onScrollBottom(); // 通知监听者滚动到底部
        }
        if (oldt != 0 && t == 0) {
            onScrollChangedListener.onScrollTop(); // 通知监听者滚动到顶部
        }


    }

    public OnScrollChangedListener getOnScrollChangedListener() {
        return onScrollChangedListener;
    }

    public void setOnScrollChangedListener(OnScrollChangedListener onScrollChangedListener) {
        this.onScrollChangedListener = onScrollChangedListener;
    }

    /**
     * 获得滚动总长度
     *
     * @return
     */
    public int getTotalVerticalScrollRange() {
        return this.computeVerticalScrollRange();
    }

//    @Override
//    protected int computeScrollDeltaToGetChildRectOnScreen(Rect rect) {
//        return 0; // 禁止ScrollView在子控件的布局改变时自动滚动
//    }
}
