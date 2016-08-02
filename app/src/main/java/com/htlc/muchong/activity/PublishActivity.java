package com.htlc.muchong.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
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
import java.util.Arrays;
import java.util.List;

import model.GoodsTypeBean;
import model.MaterialAndTypeBean;
import model.MaterialBean;
import model.PointInTimeBean;
import model.TimeBoxingBean;

/**
 * Created by sks on 2016/5/27.
 * 商品发布Activity
 */
public class PublishActivity extends BaseActivity implements AdapterView.OnItemClickListener, View.OnClickListener {
    public static final String[] TYPE_ARRAY = {"商品交易", "限时抢购", "降价拍卖", "无底拍卖", "有底拍卖"};
    public static final String[] TYPE_ARRAY_VALUE = {"1", "2", "3", "4", "5"};
    public static final String[] BAO_YOU_ARRAY = {"包邮", "不包邮"};
    public static final String[] BAO_YOU_ARRAY_VALUE = {"2", "1"};
    public static final String[] ZI_TAN_LEVEL = {"A级", "A+级", "AA级", "AA+级", "AAA级", "AAA+级","AAAA级","AAAA+级","AAAAA级", "AAAAA+级"};
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
    private LinearLayout linearBaoYou;
    private LinearLayout linearZiTanLevel;
    private TextView textType;//商品类型TextView
    private TextView textMaterial;//商品材质TextView
    private TextView textChildType;//商品小类TextView
    private TextView textStartTime;//商品开始时间TextView
    private TextView textDurationTime;//商品持续时间TextView
    private TextView textBaoYou;
    private TextView textZiTanLevel;

    private EditText editMarketPrice;//商品市场价
    private EditText editDeposit;//商品保证金
    private EditText editCount;//商品数量
    private EditText editPrice;//商品价格
    private EditText editSize;//商品规格
    private RelativeLayout relativeCount;
    private RelativeLayout relativeMarketPrice;
    private RelativeLayout relativeDeposit;
    private RelativeLayout relativePrice;


    private List<MaterialAndTypeBean> materialAndTypes;
    private List<PointInTimeBean> pointInTimes;//商品竞拍时间点列表
    private List<Pair<String, String>> qiangDays;//商品抢购日期列表

    private String type;
    private String childType;
    private String pointInTime;
    private String timeboxing;
    private String material;
    private String ziTanLevel;
    private String baoYou;
    private ProgressDialog progressDialog;



    @Override
    protected int getLayoutId() {
        return R.layout.activity_publish;
    }

    @Override
    protected void setupView() {
        mTitleTextView.setText(R.string.title_publish);
        mTitleRightTextView.setText(R.string.commit);
        mTitleRightTextView.setVisibility(View.VISIBLE);
        mTitleRightTextView.setOnClickListener(this);

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
        linearBaoYou = (LinearLayout) findViewById(R.id.linearBaoYou);
        linearZiTanLevel = (LinearLayout) findViewById(R.id.linearZiTanLevel);
        textType = (TextView) findViewById(R.id.textType);
        textChildType = (TextView) findViewById(R.id.textChildType);
        textMaterial = (TextView) findViewById(R.id.textMaterial);
        textStartTime = (TextView) findViewById(R.id.textStartTime);
        textDurationTime = (TextView) findViewById(R.id.textDurationTime);
        textBaoYou = (TextView) findViewById(R.id.textBaoYou);
        textZiTanLevel = (TextView) findViewById(R.id.textZiTanLevel);
        linearType.setOnClickListener(this);
        linearChildType.setOnClickListener(this);
        linearMaterial.setOnClickListener(this);
        linearStartTime.setOnClickListener(this);
        linearDurationTime.setOnClickListener(this);
        linearBaoYou.setOnClickListener(this);
        linearZiTanLevel.setOnClickListener(this);

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
        getMaterialAndType();
        getPointInTime();
        getQiangDays();
    }

    /*获取材质和商品小类（小类有材质决定）*/
    private void getMaterialAndType() {
        App.app.appAction.getGoodsMaterialAndType(new BaseActionCallbackListener<List<MaterialAndTypeBean>>() {
            @Override
            public void onSuccess(List<MaterialAndTypeBean> data) {
                materialAndTypes = data;
            }

            @Override
            public void onIllegalState(String errorEvent, String message) {

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

    /*图片列表的点击删除或添加*/
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (adapter.getCount() - 1 != position || adapter.getData().size() == PublishAdapter.MaxImageNumber) {
            adapter.removeData(position);
        } else {
            isPickCover = false;
            pickPhoto(SelectPhotoDialogHelper.Width_720, SelectPhotoDialogHelper.Width_Scale_4, SelectPhotoDialogHelper.Height_Scale_3);
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
                if(TextUtils.isEmpty(material)){
                    ToastUtil.showToast(App.app,"请先选择材质");
                    return;
                }
                if (materialAndTypes != null) {
                    for (int i = 0; i < materialAndTypes.size(); i++) {
                        MaterialAndTypeBean materialAndTypeBean = materialAndTypes.get(i);
                        if(material.equals(materialAndTypeBean.id)){
                            String[] goodsTypesArray = new String[materialAndTypeBean.list.size()];
                            for (int j = 0; j < materialAndTypeBean.list.size(); j++) {
                                goodsTypesArray[j] = materialAndTypeBean.list.get(j).materialsub_name;
                            }
                            showPopupWindow(v, Arrays.asList(goodsTypesArray));
                            break;
                        }
                    }

                }
                break;
            case R.id.linearMaterial:
                if (materialAndTypes != null) {
                    String[] materialsArray = new String[materialAndTypes.size()];
                    for (int i = 0; i < materialAndTypes.size(); i++) {
                        materialsArray[i] = materialAndTypes.get(i).name;
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
            case R.id.linearBaoYou:
                showPopupWindow(v, Arrays.asList(BAO_YOU_ARRAY));
                break;
            case R.id.linearZiTanLevel:
                showPopupWindow(v, Arrays.asList(ZI_TAN_LEVEL));
                break;
            case R.id.buttonCommit:
            case R.id.title_right:
                publishGoods();
                break;

        }
    }

    /*提交商品*/
    private void publishGoods() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("提交中，请稍等...");
        progressDialog.show();
        App.app.appAction.publishGoods(editTitle.getText().toString().trim(), editContent.getText().toString().trim(),
                type, childType, editSize.getText().toString().trim(), material, editPrice.getText().toString(),
                pointInTime, timeboxing, editCount.getText().toString(), editMarketPrice.getText().toString(), editDeposit.getText().toString(),
                baoYou, ziTanLevel,
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
            for (int i = 0; i < materialAndTypes.size(); i++) {
                MaterialAndTypeBean materialAndTypeBean = materialAndTypes.get(i);
                if(material.equals(materialAndTypeBean.id)){
                    childType = materialAndTypeBean.list.get(position).id;
                    String preStr = getString(R.string.publish_child_type);
                    textChildType.setText(preStr + "\t\t" + materialAndTypeBean.list.get(position).materialsub_name);
                    break;
                }
            }
        } else if (clickView == linearMaterial) {
            material = materialAndTypes.get(position).id;
            if("紫檀".equals(materialAndTypes.get(position).name)){
                linearZiTanLevel.setVisibility(View.VISIBLE);
            }else {
                linearZiTanLevel.setVisibility(View.GONE);
            }
            String preStr = getString(R.string.publish_material);
            textMaterial.setText(preStr + "\t\t" + materialAndTypes.get(position).name);
            childType = "";
            textChildType.setText(R.string.publish_child_type);
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
        } else if (clickView == linearBaoYou) {
            baoYou = BAO_YOU_ARRAY_VALUE[position];
            String preStr = getString(R.string.publish_zi_tan_level);
            textBaoYou.setText(preStr + "\t\t" + BAO_YOU_ARRAY[position]);
        }else if (clickView == linearZiTanLevel) {
            ziTanLevel = ZI_TAN_LEVEL[position];
            String preStr = getString(R.string.publish_zi_tan_level);
            textZiTanLevel.setText(preStr + "\t\t" + ZI_TAN_LEVEL[position]);
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



