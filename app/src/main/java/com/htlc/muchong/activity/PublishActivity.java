package com.htlc.muchong.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.htlc.muchong.App;
import com.htlc.muchong.R;
import com.htlc.muchong.adapter.PublishAdapter;
import com.htlc.muchong.base.BaseActivity;
import com.htlc.muchong.util.SelectPhotoDialogHelper;
import com.larno.util.ToastUtil;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import model.GoodsTypeBean;
import model.MaterialBean;
import model.PointInTimeBean;
import model.TimeBoxingBean;

/**
 * Created by sks on 2016/5/27.
 * 商品发布Activity
 */
public class PublishActivity extends BaseActivity implements AdapterView.OnItemClickListener, View.OnClickListener {
    public static final String[] TYPE_ARRAY = {"精品交易", "限时抢购", "倒拍", "无底价", "有底价"};
    public static final String[] TYPE_ARRAY_VALUE = {"1", "2", "3", "4", "5"};
    private ImageView imageViewCover;//封面图
    private GridView gridView;//商品内容图gridview
    private PublishAdapter adapter;//商品内容图adapter
    private SelectPhotoDialogHelper selectPhotoDialogHelper;//选择图片工具类
    private File coverImageFile;//封面图file
    private boolean isPickCover;//是否当前是选择封面图
    private EditText editContent;//商品内容
    private EditText editTitle;//商品标题

    private LinearLayout linearType;//商品类型（抢购等)
    private LinearLayout linearChildType;//商品小类（手串等）
    private LinearLayout linearMaterial;//商品材质
    private LinearLayout linearStartTime;//商品开始时间
    private LinearLayout linearDurationTime;//商品持续时间
    private TextView textType;//商品类型TextView
    private TextView textMaterial;//商品材质TextView
    private TextView textChildType;//商品小类TextView
    private TextView textStartTime;//商品开始时间TextView
    private TextView textDurationTime;//商品持续时间TextView

    private EditText editMarketPrice;//商品市场价
    private EditText editDeposit;//商品保证金
    private EditText editCount;//商品数量
    private EditText editPrice;//商品价格
    private EditText editSize;//商品规格
    private RelativeLayout relativeCount;
    private RelativeLayout relativeMarketPrice;
    private RelativeLayout relativeDeposit;
    private RelativeLayout relativePrice;


    private List<GoodsTypeBean> goodsTypes;//商品小类列表
    private List<MaterialBean> materials;//商品材质列表
    private List<PointInTimeBean> pointInTimes;//商品竞拍时间点列表
    private List<Pair<String, String>> qiangDays;//商品抢购日期列表

    private String type;
    private String childType;
    private String pointInTime;
    private String timeboxing;
    private String material;
    private ProgressDialog progressDialog;


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

        editTitle = (EditText) findViewById(R.id.editTitle);
        editContent = (EditText) findViewById(R.id.editContent);

        linearType = (LinearLayout) findViewById(R.id.linearType);
        linearChildType = (LinearLayout) findViewById(R.id.linearChildType);
        linearMaterial = (LinearLayout) findViewById(R.id.linearMaterial);
        linearStartTime = (LinearLayout) findViewById(R.id.linearStartTime);
        linearDurationTime = (LinearLayout) findViewById(R.id.linearDurationTime);
        textType = (TextView) findViewById(R.id.textType);
        textChildType = (TextView) findViewById(R.id.textChildType);
        textMaterial = (TextView) findViewById(R.id.textMaterial);
        textStartTime = (TextView) findViewById(R.id.textStartTime);
        textDurationTime = (TextView) findViewById(R.id.textDurationTime);
        linearType.setOnClickListener(this);
        linearChildType.setOnClickListener(this);
        linearMaterial.setOnClickListener(this);
        linearStartTime.setOnClickListener(this);
        linearDurationTime.setOnClickListener(this);

        relativeCount = (RelativeLayout) findViewById(R.id.relativeCount);
        relativeMarketPrice = (RelativeLayout) findViewById(R.id.relativeMarketPrice);
        relativeDeposit = (RelativeLayout) findViewById(R.id.relativeDeposit);
        relativePrice = (RelativeLayout) findViewById(R.id.relativePrice);
        editCount = (EditText) findViewById(R.id.editCount);
        editMarketPrice = (EditText) findViewById(R.id.editMarketPrice);
        editDeposit = (EditText) findViewById(R.id.editDeposit);
        editSize = (EditText) findViewById(R.id.editSize);
        editPrice = (EditText) findViewById(R.id.editPrice);

        findViewById(R.id.buttonCommit).setOnClickListener(this);

        initData();
    }

    @Override
    protected void initData() {
        getGoodsType();
        getMaterials();
        getPointInTime();
        getQiangDays();
    }

    /*获取材质列表*/
    private void getMaterials() {
        App.app.appAction.getGoodsMaterials(new BaseActionCallbackListener<List<MaterialBean>>() {
            @Override
            public void onSuccess(List<MaterialBean> data) {
                materials = data;
            }

            @Override
            public void onIllegalState(String errorEvent, String message) {
                ToastUtil.showToast(App.app, message);
            }
        });
    }

    /*获取抢购可选日期*/
    private void getQiangDays() {
        App.app.appAction.qiangTimeList(new BaseActionCallbackListener<List<Pair<String, String>>>() {
            @Override
            public void onSuccess(List<Pair<String, String>> data) {
                qiangDays = data;
            }

            @Override
            public void onIllegalState(String errorEvent, String message) {
                ToastUtil.showToast(App.app, message);
            }
        });
    }

    /*获取竞拍时间点*/
    private void getPointInTime() {
        App.app.appAction.getPointInTimes(new BaseActionCallbackListener<List<PointInTimeBean>>() {
            @Override
            public void onSuccess(List<PointInTimeBean> data) {
                pointInTimes = data;
            }

            @Override
            public void onIllegalState(String errorEvent, String message) {
                ToastUtil.showToast(App.app, message);
            }
        });
    }

    /*获取商品小类*/
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
            pickPhoto(SelectPhotoDialogHelper.Width_720, SelectPhotoDialogHelper.Width_Scale_12, SelectPhotoDialogHelper.Height_Scale_5);
        }
    }

    /*点击时间处理*/
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imageViewCover:
                isPickCover = true;
                pickPhoto(SelectPhotoDialogHelper.Width_720, SelectPhotoDialogHelper.Width_Scale_4, SelectPhotoDialogHelper.Height_Scale_3);
                break;
            case R.id.linearType:
                showPopupWindow(v, Arrays.asList(TYPE_ARRAY));
                break;
            case R.id.linearChildType:
                if (goodsTypes != null) {
                    String[] goodsTypesArray = new String[goodsTypes.size()];
                    for (int i = 0; i < goodsTypes.size(); i++) {
                        goodsTypesArray[i] = goodsTypes.get(i).constant_name;
                    }
                    showPopupWindow(v, Arrays.asList(goodsTypesArray));
                }
                break;
            case R.id.linearMaterial:
                if (materials != null) {
                    String[] materialsArray = new String[materials.size()];
                    for (int i = 0; i < materials.size(); i++) {
                        materialsArray[i] = materials.get(i).name;
                    }
                    showPopupWindow(v, Arrays.asList(materialsArray));
                }
                break;
            case R.id.linearStartTime:
                if (TYPE_ARRAY_VALUE[1].equals(type)) {
                    if (qiangDays != null) {
                        String[] qiangDaysArray = new String[qiangDays.size()];
                        for (int i = 0; i < qiangDays.size(); i++) {
                            qiangDaysArray[i] = qiangDays.get(i).second;
                        }
                        showPopupWindow(v, Arrays.asList(qiangDaysArray));
                    }
                } else {
                    if (pointInTimes != null) {
                        String[] pointInTimeArray = new String[pointInTimes.size()];
                        for (int i = 0; i < pointInTimes.size(); i++) {
                            pointInTimeArray[i] = pointInTimes.get(i).constant_time;
                        }
                        showPopupWindow(v, Arrays.asList(pointInTimeArray));
                    }
                }
                break;
            case R.id.linearDurationTime:
                showPopupWindow(v, Arrays.asList(TimeBoxingBean.TIME_DESCRIPTION_ARRAY));
                break;
            case R.id.buttonCommit:
                publishGoods();
                break;

        }
    }

    /*提交商品*/
    private void publishGoods() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("提交中，请稍等...");
        progressDialog.show();
        boolean exists = coverImageFile.exists();
        App.app.appAction.publishGoods(editTitle.getText().toString().trim(), editContent.getText().toString().trim(),
                type, childType, editSize.getText().toString().trim(), material, editPrice.getText().toString(),
                pointInTime, timeboxing, editCount.getText().toString(), editMarketPrice.getText().toString(), editDeposit.getText().toString(),
                coverImageFile, adapter.getData(), new BaseActionCallbackListener<Void>() {
                    @Override
                    public void onSuccess(Void data) {
                        if (progressDialog != null) {
                            progressDialog.dismiss();
                        }
                        ToastUtil.showToast(App.app, "提交成功");
                        finish();
                    }

                    @Override
                    public void onIllegalState(String errorEvent, String message) {
                        if (progressDialog != null) {
                            progressDialog.dismiss();
                        }
                        ToastUtil.showToast(App.app, message);
                    }
                }
        );
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
        if (clickView == linearType) {
            type = (position + 1) + "";
            String preStr = getString(R.string.publish_type);
            textType.setText(preStr + "\t\t" + TYPE_ARRAY[position]);
            if (position == 0) {
                linearStartTime.setVisibility(View.GONE);
                linearDurationTime.setVisibility(View.GONE);
                relativeCount.setVisibility(View.GONE);
                relativeMarketPrice.setVisibility(View.GONE);
                relativeDeposit.setVisibility(View.GONE);
                relativePrice.setVisibility(View.VISIBLE);
            } else if (position == 1) {
                linearStartTime.setVisibility(View.VISIBLE);
                linearDurationTime.setVisibility(View.GONE);
                relativeCount.setVisibility(View.VISIBLE);
                relativeMarketPrice.setVisibility(View.VISIBLE);
                relativeDeposit.setVisibility(View.GONE);
                relativePrice.setVisibility(View.VISIBLE);
            } else if (position == 2) {
                linearStartTime.setVisibility(View.VISIBLE);
                linearDurationTime.setVisibility(View.VISIBLE);
                relativeCount.setVisibility(View.GONE);
                relativeMarketPrice.setVisibility(View.VISIBLE);
                relativeDeposit.setVisibility(View.VISIBLE);
                relativePrice.setVisibility(View.VISIBLE);
            } else if (position == 3) {
                linearStartTime.setVisibility(View.VISIBLE);
                linearDurationTime.setVisibility(View.VISIBLE);
                relativeCount.setVisibility(View.GONE);
                relativeMarketPrice.setVisibility(View.VISIBLE);
                relativeDeposit.setVisibility(View.VISIBLE);
                relativePrice.setVisibility(View.GONE);
            } else if (position == 4) {
                linearStartTime.setVisibility(View.VISIBLE);
                linearDurationTime.setVisibility(View.VISIBLE);
                relativeCount.setVisibility(View.GONE);
                relativeMarketPrice.setVisibility(View.VISIBLE);
                relativeDeposit.setVisibility(View.VISIBLE);
                relativePrice.setVisibility(View.VISIBLE);
            }
        } else if (clickView == linearChildType) {
            childType = goodsTypes.get(position).id;
            String preStr = getString(R.string.publish_child_type);
            textChildType.setText(preStr + "\t\t" + goodsTypes.get(position).constant_name);
        } else if (clickView == linearMaterial) {
            material = materials.get(position).id;
            String preStr = getString(R.string.publish_material);
            textMaterial.setText(preStr + "\t\t" + materials.get(position).name);
        } else if (clickView == linearStartTime) {
            if (TYPE_ARRAY_VALUE[1].equals(type)) {
                pointInTime = qiangDays.get(position).second;
                String preStr = getString(R.string.publish_start_time);
                textStartTime.setText(preStr + "\t\t" + qiangDays.get(position).second);
            } else {
                pointInTime = pointInTimes.get(position).constant_time;
                String preStr = getString(R.string.publish_start_time);
                textStartTime.setText(preStr + "\t\t" + pointInTimes.get(position).constant_time);
            }
        } else if (clickView == linearDurationTime) {
            timeboxing = TimeBoxingBean.TIME_ARRAY[position];
            String preStr = getString(R.string.publish_duration_time);
            textDurationTime.setText(preStr + "\t\t" + TimeBoxingBean.TIME_DESCRIPTION_ARRAY[position]);
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
    private void pickPhoto(int width, int aspectX, int aspectY) {
        selectPhotoDialogHelper = new SelectPhotoDialogHelper(this, new PublishOnPickPhotoFinishListener(), width, aspectX, aspectY);
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



