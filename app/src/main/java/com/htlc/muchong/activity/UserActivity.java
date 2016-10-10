package com.htlc.muchong.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
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
 * 用户信息Activity
 */
public class UserActivity extends BaseActivity implements View.OnClickListener {
    private static final int Request_Name = 101;
    private static final int Request_Area = 102;
    private ImageView imageHead;//用户头像
    private SelectPhotoDialogHelper selectPhotoDialogHelper;//选择图片工具类
    private View relativeName, relativeArea,relativePassword,relativeTel,relativeAddress;//昵称，地区，修改密码，修改手机，收获地址
    private TextView textName, textArea;//昵称，地区
    private File imageFile;//选择的图像文件

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
                Intent intent1 = new Intent(this, UpdateInfoAreaActivity.class);
                intent1.putExtra(UpdateInfoAreaActivity.ActivityTitleId,R.string.user_area);
                intent1.putExtra(UpdateInfoAreaActivity.Hint,R.string.user_area_hint);
                intent1.putExtra(UpdateInfoAreaActivity.Value,textArea.getText().toString());
                startActivityForResult(intent1, Request_Area);
                break;
            case R.id.relativeTel:
                Intent intent2 = new Intent(this, ResetTelActivity.class);
                startActivity(intent2);
                break;
            case R.id.relativePassword:
                Intent intent3 = new Intent(this, RegisterActivity.class);
                intent3.putExtra(RegisterActivity.IsResetPassword, true);
                startActivity(intent3);
                break;
            case R.id.relativeAddress:
                Intent intent4 = new Intent(this, AddressActivity.class);
                startActivity(intent4);
                break;
            case R.id.textButton:
                commit();
                break;
        }
    }

    /*保存用户信息*/
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
            //修改名称
            if(requestCode == Request_Name){
                textName.setText(data.getStringExtra(UpdateInfoActivity.Value));
            } else if(requestCode == Request_Area){//修改地区
                textArea.setText(data.getStringExtra(UpdateInfoActivity.Value));
            }else if(selectPhotoDialogHelper!=null){//选择图片
                selectPhotoDialogHelper.onActivityResult(requestCode, resultCode, data);
            }
        }
    }

    /*选择照片结束的回调*/
    private class OnPickPhotoFinishListener implements SelectPhotoDialogHelper.OnPickPhotoFinishListener {
        @Override
        public void onPickPhotoFinishListener(File imageFile) {
            if(imageFile!=null){
                UserActivity.this.imageFile = imageFile;
                Log.e("imageFile---",""+imageFile);
                Picasso.with(UserActivity.this).load(Uri.fromFile(imageFile)).transform(new CircleTransform()).into(imageHead);
            }
        }
    }
}
