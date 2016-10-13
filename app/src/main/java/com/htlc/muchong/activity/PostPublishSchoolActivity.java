package com.htlc.muchong.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
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
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.htlc.muchong.App;
import com.htlc.muchong.R;
import com.htlc.muchong.adapter.PublishAdapter;
import com.htlc.muchong.adapter.PublishSchoolAdapter;
import com.htlc.muchong.base.BaseActivity;
import com.htlc.muchong.util.LogUtils;
import com.htlc.muchong.util.SelectPhotoDialogHelper;
import com.larno.util.ToastUtil;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import model.OrderPayEvent;
import model.PSchoolEvent;

/**
 *论坛帖子发布Activity之学堂页
 */
public class PostPublishSchoolActivity extends BaseActivity implements AdapterView.OnItemClickListener, View.OnClickListener  {


    public static void goPostPublishSchoolActivity(Context context, String publishType, int titleId) {
        Intent intent = new Intent(context, PostPublishSchoolActivity.class);
        intent.putExtra(Publish_Type, publishType);
        intent.putExtra(ActivityTitleId, titleId);
        context.startActivity(intent);
    }
    public static final String Publish_Type = "Publish_Type";
    public static final String[] Publish_Types = {"5", "5", "4", "3"};
    private LinearLayout linearCover;
    private ImageView imageViewCover,imageCover;
    private ListView lsitView;
    private PublishSchoolAdapter adapter;
    private SelectPhotoDialogHelper selectPhotoDialogHelper;
    private File coverImageFile;
    private boolean isPickCover;
    private EditText editTitle;


    private ProgressDialog progressDialog;
    private String publishType;//发布类型
    private String selectDurationTime;//活动时间长度
    String string;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_postpublishschool;
    }

    @Override
    protected void setupView() {
        Intent intent = getIntent();
        publishType = intent.getStringExtra(Publish_Type);
        mTitleTextView.setText(intent.getIntExtra(ActivityTitleId, R.string.title_post_publish));
        mTitleRightTextView.setText(R.string.commit);
        mTitleRightTextView.setVisibility(View.VISIBLE);
        mTitleRightTextView.setOnClickListener(this);

        imageViewCover = (ImageView) findViewById(R.id.imageViewCover);
        imageViewCover.setOnClickListener(this);
        imageCover = (ImageView) findViewById(R.id.imageCover);
        imageCover.setOnClickListener(this);
        lsitView = (ListView) findViewById(R.id.pps_listView);
        adapter = new PublishSchoolAdapter();
        lsitView.setAdapter(adapter);
        lsitView.setOnItemClickListener(this);
        editTitle = (EditText) findViewById(R.id.editTitle);

        initData();
    }

    @Override
    protected void initData() {

    }

    /*图片列表的点击删除或添加*/
    @Override
    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
//        LogUtils.e("view---",""+view);
//        ImageView et = (ImageView) view.findViewById(R.id.ps_imageView);// 从layout中获得控件,根据其id
//        et.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                LogUtils.e("v---",""+v);
//                adapter.removeData(position);
//            }
//        });


    }

    /*点击时间处理*/
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imageViewCover:
                isPickCover = true;
                pickPhoto(SelectPhotoDialogHelper.Width_720, SelectPhotoDialogHelper.Width_Scale_4, SelectPhotoDialogHelper.Height_Scale_3);
                break;

            case R.id.title_right:
                initString();//获取图片输入文本

                break;
            case R.id.imageCover:
                isPickCover = false;
                pickPhoto(SelectPhotoDialogHelper.Width_720, SelectPhotoDialogHelper.Width_Scale_4, SelectPhotoDialogHelper.Height_Scale_3);
                break;

        }
    }

    private void initString() {
        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < lsitView.getChildCount(); i++) {
            LinearLayout layout = (LinearLayout)lsitView.getChildAt(i);// 获得子item的layout
            EditText et = (EditText) layout.findViewById(R.id.ps_et);// 从layout中获得控件,根据其id
            LogUtils.e("et---",""+et.getText().toString());
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("forum_content",et.getText().toString());
            jsonArray.add(jsonObject);
        }
        string = jsonArray.toString();
        LogUtils.e("string---",""+string);
        publishCang();
    }


    /*提交藏品*/
    private void publishCang() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("提交中，请稍等...");
        progressDialog.show();
        App.app.appAction.publishPost(publishType,selectDurationTime, editTitle.getText().toString().trim(), string, coverImageFile, adapter.getData(), new BaseActionCallbackListener<Void>() {
            @Override
            public void onSuccess(Void data) {
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }
                LogUtils.e("onSuccess---", "onSuccess");
                ToastUtil.showToast(App.app, "发布成功！");
                finish();
            }

            @Override
            public void onIllegalState(String errorEvent, String message) {
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }
                ToastUtil.showToast(App.app, message);
            }
        });

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
                    Picasso.with(PostPublishSchoolActivity.this).load(Uri.fromFile(coverImageFile)).resizeDimen(R.dimen.publish_grid_view_size, R.dimen.publish_grid_view_size).centerCrop().into(imageViewCover);
                } else {
                    adapter.setData(Arrays.asList(imageFile), true);
                }
            }
        }
    }

}
