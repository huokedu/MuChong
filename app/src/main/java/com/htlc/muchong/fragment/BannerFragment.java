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

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import model.BannerBean;


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
    private List<BannerBean> list = new ArrayList<>();
    private int currentItem;
    private onItemClickListener listener;
    private boolean recycle = true;

    interface onItemClickListener{
        void onItemClickListener(BannerBean bean);
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

    public void setData(List<BannerBean> data){
        list.clear();
        list.addAll(data);
        adapter.notifyDataSetChanged();

        indicatorLayout.removeAllViews();
        for (int i = 0; i < adapter.getCount(); i++) {
            ImageView pointer = (ImageView) View.inflate(getActivity(), R.layout.adapter_banner_point, null);
            indicatorLayout.addView(pointer);
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
            final BannerBean bean = (BannerBean) list.get(position);
//            Picasso.with(getContext().getApplicationContext()).load(Uri.parse("http://t2.damaimob.com/upload/morepic/573171eac7fef.png")).into(view);
            view.setImageResource(R.mipmap.ic_launcher);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener!=null){
                        listener.onItemClickListener(bean);
                    }
                }
            });
            container.addView(view);
            return view;
        }
    }
}