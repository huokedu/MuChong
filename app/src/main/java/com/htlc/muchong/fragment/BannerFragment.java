package com.htlc.muchong.fragment;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.htlc.muchong.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


/** 
 * 实现可循环，可轮播的viewpager 
 */  
@SuppressLint("NewApi")  
public class BannerFragment extends Fragment implements OnPageChangeListener {
    public static final int CycleCode = 100;
    public static final int DelayTime = 2000;

    private LinearLayout indicatorLayout; // 指示器  
    private ViewPager viewPager;
    private BannerPagerAdapter adapter;
    private List<String> list = new ArrayList<>();
    private int currentItem;
    private onItemClickListener listener;
    private boolean recycle = true;

    public interface onItemClickListener{
        void onItemClickListener(int position);
    }
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what==CycleCode){
                if(currentItem==list.size()-1){
                    viewPager.setCurrentItem(0,false);
                }else {
                    viewPager.setCurrentItem(currentItem+1);
                }
                handler.sendEmptyMessageDelayed(CycleCode,DelayTime);
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,  
            Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(
                R.layout.fragment_banner, null);

        viewPager = (ViewPager) view.findViewById(R.id.viewPager);
        indicatorLayout = (LinearLayout) view.findViewById(R.id.linearPointContainer);

        adapter = new BannerPagerAdapter();
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(this);

        return view;  
    }

    @Override
    public void onStart() {
        super.onStart();
        if(recycle){
            handler.sendEmptyMessageDelayed(CycleCode,DelayTime);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        handler.removeCallbacksAndMessages(null);
    }

    public void setRecycle(boolean recycle) {
        this.recycle = recycle;
    }

    public void setListener(onItemClickListener listener) {
        this.listener = listener;
    }

    public void setData(List<String> data){
        list.clear();
        list.addAll(data);
        adapter.notifyDataSetChanged();

        indicatorLayout.removeAllViews();
        for (int i = 0; i < adapter.getCount(); i++) {
            LayoutInflater.from(getContext()).inflate(R.layout.adapter_banner_point,indicatorLayout,true);
        }
        currentItem = viewPager.getCurrentItem();
        ImageView imageView = (ImageView) indicatorLayout.getChildAt(currentItem);
        if(imageView != null){
            imageView.setSelected(true);
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        if (currentItem != position) {
            indicatorLayout.getChildAt(currentItem).setSelected(false);
            indicatorLayout.getChildAt(position).setSelected(true);
            currentItem = position;
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }


    public class BannerPagerAdapter extends PagerAdapter{

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view==object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            ImageView view = (ImageView) View.inflate(getActivity(), R.layout.adapter_banner, null);
            String uri = list.get(position);
            Picasso.with(getContext()).load(Uri.parse(uri)).placeholder(R.mipmap.default_banner).error(R.mipmap.default_banner).into(view);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener!=null){
                        listener.onItemClickListener(position);
                    }
                }
            });
            container.addView(view);
            return view;
        }
    }
}