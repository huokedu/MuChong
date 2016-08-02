package com.htlc.muchong.fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.htlc.muchong.R;
import com.htlc.muchong.activity.ProductListActivity;
import com.htlc.muchong.adapter.SecondAdapter;

import model.GoodsTypeBean;

/**
 * Created by sks on 2016/5/17.
 * 商城Fragment
 *
 * 继承首页Fragment的所有界面和数据
 * 重写banner下边展示的分类
 */
public class SecondFragment extends FirstFragment{
    private GridView gridView;
    private SecondAdapter adapter;

    /*重写父类的初始化banner下边  类别容器的方法，显示自己的类别*/
    @Override
    protected void initLinearType() {
        LayoutInflater.from(getContext()).inflate(R.layout.layout_second_type, mlinearTypeContainer, true);
        gridView = (GridView) mlinearTypeContainer.findViewById(R.id.gridView);
        adapter = new SecondAdapter();
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                GoodsTypeBean bean = (GoodsTypeBean) adapter.getItem(position);
                ProductListActivity.goProductListActivity(getActivity(),bean.id,bean.name);
            }
        });
    }

    @Override
    protected void refreshView() {
        super.refreshView();
        adapter.setData(homeBean.smallclass,false);
    }
}
