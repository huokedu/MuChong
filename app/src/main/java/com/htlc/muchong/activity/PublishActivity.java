package com.htlc.muchong.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.htlc.muchong.App;
import com.htlc.muchong.R;
import com.htlc.muchong.adapter.PublishAdapter;
import com.htlc.muchong.base.BaseActivity;
import com.htlc.muchong.util.LoginUtil;
import com.htlc.muchong.util.SelectPhotoDialogHelper;
import com.larno.util.ToastUtil;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import model.GoodsTypeBean;

/**
 * Created by sks on 2016/5/27.
 */
public class PublishActivity extends BaseActivity implements AdapterView.OnItemClickListener, View.OnClickListener {
    public static final String[] TYPE_ARRAY = {"精品交易", "限时抢购", "倒拍", "无底价", "有底价"};
    private ImageView imageViewCover;
    private GridView gridView;
    private PublishAdapter adapter;
    private SelectPhotoDialogHelper selectPhotoDialogHelper;
    private File coverImageFile;
    private boolean isPickCover;
    private LinearLayout linearType;
    private LinearLayout linearChildType;
    private LinearLayout linearStartTime;
    private LinearLayout linearDurationTime;
    private TextView textType;
    private TextView textChildType;
    private TextView textStartTime;
    private TextView textDurationTime;

    private List<GoodsTypeBean> goodsTypes;

    private String type;
    private String childType;
    private String startTime;
    private String durationTime;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_publish;
    }

    @Override
    protected void setupView() {
        mTitleTextView.setText(R.string.title_publish);

        imageViewCover = (ImageView) findViewById(R.id.imageViewCover);
        imageViewCover.setOnClickListener(this);
        gridView = (GridView) findViewById(R.id.gridView);
        adapter = new PublishAdapter();
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(this);

        linearType = (LinearLayout) findViewById(R.id.linearType);
        linearChildType = (LinearLayout) findViewById(R.id.linearChildType);
        linearStartTime = (LinearLayout) findViewById(R.id.linearStartTime);
        linearDurationTime = (LinearLayout) findViewById(R.id.linearDurationTime);
        textType = (TextView) findViewById(R.id.textType);
        textChildType = (TextView) findViewById(R.id.textChildType);
        textStartTime = (TextView) findViewById(R.id.textStartTime);
        textDurationTime = (TextView) findViewById(R.id.textDurationTime);
        linearType.setOnClickListener(this);
        linearChildType.setOnClickListener(this);
        linearStartTime.setOnClickListener(this);
        linearDurationTime.setOnClickListener(this);

        initData();
    }

    @Override
    protected void initData() {
        getGoodsType();
    }

    private void getGoodsType() {
        App.app.appAction.getGoodsType(new BaseActionCallbackListener<List<GoodsTypeBean>>() {
            @Override
            public void onSuccess(List<GoodsTypeBean> data) {
                goodsTypes = data;
            }
            @Override
            public void onIllegalState(String errorEvent, String message) {
                ToastUtil.showToast(App.app, message);
            }
        });
    }

    /*图片列表的点击删除或添加*/
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (adapter.getCount() - 1 != position || adapter.getData().size() == PublishAdapter.MaxImageNumber) {
            adapter.removeData(position);
        } else {
            isPickCover = false;
            pickPhoto();
        }
    }

    /*点击时间处理*/
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imageViewCover:
                isPickCover = true;
                pickPhoto();
                break;
            case R.id.linearType:
                showPopupWindow(v, Arrays.asList(TYPE_ARRAY));
                break;
            case R.id.linearChildType:
                if(goodsTypes!=null){
                    String[] goodsTypesArray = new String[goodsTypes.size()];
                    for(int i=0; i<goodsTypes.size(); i++){
                        goodsTypesArray[i] = goodsTypes.get(i).constant_name;
                    }
                    showPopupWindow(v, Arrays.asList(goodsTypesArray));
                }
                break;
            case R.id.linearStartTime:
                showPopupWindow(v, Arrays.asList(TYPE_ARRAY));
                break;
            case R.id.linearDurationTime:
                showPopupWindow(v, Arrays.asList(TYPE_ARRAY));
                break;

        }
    }

    /*弹出选择条目*/
    private void showPopupWindow(final View clickView, List<String> list) {
        ListView listView = (ListView) LayoutInflater.from(this).inflate(R.layout.pop_window_publish_list, null);
        // 设置按钮的点击事件
        PublishPickItemAdapter adapter = new PublishPickItemAdapter(list);
        listView.setAdapter(adapter);
        int width = clickView.getWidth();
        final PopupWindow popupWindow = new PopupWindow(listView, width, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (popupWindow != null) {
                    popupWindow.dismiss();
                }
                selectItem(clickView, position);
            }
        });
        popupWindow.setTouchable(true);
        popupWindow.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });
        // 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
        popupWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.pai_eidit_retangle_shape));
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                switchSelectItemStatus(clickView, false);
            }
        });
        popupWindow.showAsDropDown(clickView, 0, 0);
        switchSelectItemStatus(clickView, true);

    }

    /*选中条目*/
    private void selectItem(View clickView, int position) {
        if(clickView == linearType){
            type = position+"";
            textType.setText(TYPE_ARRAY[position]);
        }else if(clickView == linearChildType){
            childType = goodsTypes.get(position).id;
            textChildType.setText(TYPE_ARRAY[position]);
        }else if(clickView == linearStartTime){
            textStartTime.setText(TYPE_ARRAY[position]);
        }else if(clickView == linearDurationTime){
            textDurationTime.setText(TYPE_ARRAY[position]);
        }
    }

    /*切换选中条目的状态*/
    private void switchSelectItemStatus(View clickView, boolean flag) {
        clickView.setSelected(flag);
        if (clickView != null && clickView instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) clickView;
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                viewGroup.getChildAt(i).setSelected(flag);
            }
        }
    }

    /*弹窗选择图片对话框*/
    private void pickPhoto() {
        selectPhotoDialogHelper = new SelectPhotoDialogHelper(this, new PublishOnPickPhotoFinishListener(), 800, 4, 3);
        selectPhotoDialogHelper.showPickPhotoDialog();
    }

    /*选择图片的Result*/
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (selectPhotoDialogHelper != null) {
                selectPhotoDialogHelper.onActivityResult(requestCode, resultCode, data);
            }
        }
    }

    /*选择图片监听器*/
    class PublishOnPickPhotoFinishListener implements SelectPhotoDialogHelper.OnPickPhotoFinishListener {
        @Override
        public void onPickPhotoFinishListener(File imageFile) {
            if (imageFile != null) {
                if (isPickCover) {
                    coverImageFile = imageFile;
                    Picasso.with(PublishActivity.this).load(Uri.fromFile(coverImageFile)).resizeDimen(R.dimen.publish_grid_view_size, R.dimen.publish_grid_view_size).centerCrop().into(imageViewCover);
                } else {
                    adapter.setData(Arrays.asList(imageFile), true);
                }
            }
        }
    }

    /*选择条目Adapter*/
    class PublishPickItemAdapter extends BaseAdapter {
        private List<String> list;

        public PublishPickItemAdapter(List<String> list) {
            this.list = list;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = View.inflate(parent.getContext(), R.layout.adapter_publish_pick_item, null);
            }
            String value = list.get(position);
            ((TextView) convertView).setText(value);
            return convertView;
        }
    }
}



