package com.htlc.muchong.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.htlc.muchong.App;
import com.htlc.muchong.R;
import com.htlc.muchong.base.BaseActivity;
import com.htlc.muchong.util.CircleTransform;
import com.htlc.muchong.util.LoginUtil;
import com.htlc.muchong.util.SelectPhotoDialogHelper;
import com.larno.util.ToastUtil;
import com.squareup.picasso.Picasso;

import java.io.File;

import model.UserInfoBean;

/**
 * Created by sks on 2016/5/27.
 */
public class UserActivity extends BaseActivity implements View.OnClickListener {
    private static final int Request_Name = 101;
    private static final int Request_Area = 102;
    private ImageView imageHead;
    private SelectPhotoDialogHelper selectPhotoDialogHelper;
    private View relativeName, relativeArea,relativePassword,relativeTel,relativeAddress;
    private TextView textName, textArea;
    private File imageFile;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_user;
    }

    @Override
    protected void setupView() {
        mTitleTextView.setText(R.string.title_user);

        textName = (TextView) findViewById(R.id.textName);
        textArea = (TextView) findViewById(R.id.textArea);

        imageHead = (ImageView) findViewById(R.id.imageHead);
        relativeName = findViewById(R.id.relativeName);
        relativeArea = findViewById(R.id.relativeArea);
        relativePassword = findViewById(R.id.relativePassword);
        relativeTel = findViewById(R.id.relativeTel);
        relativeAddress = findViewById(R.id.relativeAddress);
        imageHead.setOnClickListener(this);
        relativeName.setOnClickListener(this);
        relativeArea.setOnClickListener(this);
        relativePassword.setOnClickListener(this);
        relativeTel.setOnClickListener(this);
        relativeAddress.setOnClickListener(this);
        findViewById(R.id.textButton).setOnClickListener(this);
        initData();
    }

    @Override
    protected void initData() {
        UserInfoBean userInfo = LoginUtil.getUserInfo();
        Picasso.with(this).load(Uri.parse(userInfo.userinfo_headportrait)).transform(new CircleTransform()).error(R.mipmap.default_fourth_two_head).into(imageHead);
        textName.setText(userInfo.userinfo_nickname);
        textArea.setText(userInfo.userinfo_address);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imageHead:
                selectPhotoDialogHelper = new SelectPhotoDialogHelper(this, new OnPickPhotoFinishListener(),300,1,1);
                selectPhotoDialogHelper.showPickPhotoDialog();
                break;
            case R.id.relativeName:
                Intent intent = new Intent(this, UpdateInfoActivity.class);
                intent.putExtra(UpdateInfoActivity.ActivityTitleId,R.string.user_nick);
                intent.putExtra(UpdateInfoActivity.Hint,R.string.user_nick_hint);
                intent.putExtra(UpdateInfoActivity.Value,textName.getText().toString());
                startActivityForResult(intent, Request_Name);
                break;
            case R.id.relativeArea:
                Intent intent1 = new Intent(this, UpdateInfoActivity.class);
                intent1.putExtra(UpdateInfoActivity.ActivityTitleId,R.string.user_area);
                intent1.putExtra(UpdateInfoActivity.Hint,R.string.user_area_hint);
                intent1.putExtra(UpdateInfoActivity.Value,textArea.getText().toString());
                startActivityForResult(intent1, Request_Area);
                break;
            case R.id.relativeTel:

                break;
            case R.id.relativePassword:

                break;
            case R.id.relativeAddress:

                break;
            case R.id.textButton:
                commit();
                break;
        }
    }

    private void commit() {
        App.app.appAction.updateUserInfo(textName.getText().toString(), textArea.getText().toString(), imageFile, new BaseActionCallbackListener<Void>() {
            @Override
            public void onSuccess(Void data) {
                ToastUtil.showToast(App.app,"保存成功");
            }

            @Override
            public void onIllegalState(String errorEvent, String message) {
                ToastUtil.showToast(App.app,message);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if(requestCode == Request_Name){
                textName.setText(data.getStringExtra(UpdateInfoActivity.Value));
            } else if(requestCode == Request_Area){
                textArea.setText(data.getStringExtra(UpdateInfoActivity.Value));
            }else if(selectPhotoDialogHelper!=null){
                selectPhotoDialogHelper.onActivityResult(requestCode, resultCode, data);
            }
        }
    }

    private class OnPickPhotoFinishListener implements SelectPhotoDialogHelper.OnPickPhotoFinishListener {
        @Override
        public void onPickPhotoFinishListener(File imageFile) {
            if(imageFile!=null){
                UserActivity.this.imageFile = imageFile;
                Picasso.with(UserActivity.this).load(Uri.fromFile(imageFile)).transform(new CircleTransform()).into(imageHead);
            }
        }
    }
}
