package com.htlc.muchong.fragment;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.htlc.muchong.App;
import com.htlc.muchong.R;
import com.htlc.muchong.activity.ProductListActivity;
import com.htlc.muchong.adapter.SecondAdapter;
import com.larno.util.ToastUtil;

import java.util.Arrays;

/**
 * Created by sks on 2016/5/17.
 */
public class SecondFragment extends FirstFragment{
    private GridView gridView;
    private SecondAdapter adapter;
    public static final String[] sampleNetworkImageURLs = {
            "http://pic55.nipic.com/file/20141208/19462408_171130083000_2.jpg",
            "http://pic36.nipic.com/20131217/6704106_233034463381_2.jpg",
            "http://img05.tooopen.com/images/20140604/sy_62331342149.jpg",
            "http://pic55.nipic.com/file/20141208/19462408_171130083000_2.jpg",
            "http://pic36.nipic.com/20131217/6704106_233034463381_2.jpg",
            "http://img05.tooopen.com/images/20140604/sy_62331342149.jpg",
            "http://pic36.nipic.com/20131217/6704106_233034463381_2.jpg"
    };
    @Override
    protected void initLinearType() {
        LayoutInflater.from(getContext()).inflate(R.layout.layout_second_type, mlinearTypeContainer, true);
        gridView = (GridView) mlinearTypeContainer.findViewById(R.id.gridView);
        adapter = new SecondAdapter();
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ToastUtil.showToast(App.app, "gridView position = " + position);
                startActivity(new Intent(getActivity(), ProductListActivity.class));
            }
        });
    }

    @Override
    protected void refreshView() {
        super.refreshView();
        adapter.setData(homeBean.smallclass,false);
    }
}
