package com.htlc.muchong.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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
    private ImageView imageViewCover;
    private GridView gridView;
    private PublishAdapter adapter;
    private SelectPhotoDialogHelper selectPhotoDialogHelper;
    private File coverImageFile;
    private boolean isPickCover;
    private EditText editTitle;


    private ProgressDialog progressDialog;
    private String publishType;//发布类型
    private String selectDurationTime;//活动时间长度

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
        gridView = (GridView) findViewById(R.id.gridView);
        adapter = new PublishAdapter();
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(this);
        editTitle = (EditText) findViewById(R.id.editTitle);

        initData();
    }

    @Override
    protected void initData() {

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

            case R.id.title_right:
                publishCang();
                break;

        }
    }


    /*提交藏品*/
    private void publishCang() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("提交中，请稍等...");
        progressDialog.show();
        String st = null;
//        st = editContent.getText().toString().trim();
        App.app.appAction.publishPost(publishType,selectDurationTime, editTitle.getText().toString().trim(), st, coverImageFile, adapter.getData(), new BaseActionCallbackListener<Void>() {
            @Override
            public void onSuccess(Void data) {
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }
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
