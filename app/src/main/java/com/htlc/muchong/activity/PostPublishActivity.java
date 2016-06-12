package com.htlc.muchong.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;

import com.htlc.muchong.App;
import com.htlc.muchong.R;
import com.htlc.muchong.adapter.PublishAdapter;
import com.htlc.muchong.base.BaseActivity;
import com.htlc.muchong.util.SelectPhotoDialogHelper;
import com.larno.util.ToastUtil;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.Arrays;

/**
 * Created by sks on 2016/5/27.
 */
public class PostPublishActivity extends BaseActivity implements AdapterView.OnItemClickListener, View.OnClickListener {
    private ImageView imageViewCover;
    private GridView gridView;
    private PublishAdapter adapter;
    private SelectPhotoDialogHelper selectPhotoDialogHelper;
    private File coverImageFile;
    private boolean isPickCover;
    private EditText editContent;
    private EditText editTitle;

    private ProgressDialog progressDialog;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_post_publish;
    }

    @Override
    protected void setupView() {
        mTitleTextView.setText(R.string.title_post_publish);

        imageViewCover = (ImageView) findViewById(R.id.imageViewCover);
        imageViewCover.setOnClickListener(this);
        gridView = (GridView) findViewById(R.id.gridView);
        adapter = new PublishAdapter();
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(this);

        editTitle = (EditText) findViewById(R.id.editTitle);
        editContent = (EditText) findViewById(R.id.editContent);


        findViewById(R.id.buttonCommit).setOnClickListener(this);

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
            case R.id.buttonCommit:
                publishCang();
                break;

        }
    }

    /*提交藏品*/
    private void publishCang() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("提交中，请稍等...");
        progressDialog.show();
        App.app.appAction.publishPostCang(true,editTitle.getText().toString().trim(), editContent.getText().toString().trim(), coverImageFile, adapter.getData(), new BaseActionCallbackListener<Void>() {
            @Override
            public void onSuccess(Void data) {
                if(progressDialog!=null){
                    progressDialog.dismiss();
                }
                ToastUtil.showToast(App.app,"发布成功！");
            }

            @Override
            public void onIllegalState(String errorEvent, String message) {
                if(progressDialog!=null){
                    progressDialog.dismiss();
                }
                ToastUtil.showToast(App.app,message);
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
                    Picasso.with(PostPublishActivity.this).load(Uri.fromFile(coverImageFile)).resizeDimen(R.dimen.publish_grid_view_size, R.dimen.publish_grid_view_size).centerCrop().into(imageViewCover);
                } else {
                    adapter.setData(Arrays.asList(imageFile), true);
                }
            }
        }
    }
}



