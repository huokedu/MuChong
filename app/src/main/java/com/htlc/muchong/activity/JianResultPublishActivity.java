package com.htlc.muchong.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.htlc.muchong.App;
import com.htlc.muchong.R;
import com.htlc.muchong.base.BaseActivity;
import com.larno.util.ToastUtil;

/**
 * Created by sks on 2016/5/27.
 */
public class JianResultPublishActivity extends BaseActivity implements View.OnClickListener {
    public static final String Post_Id = "Post_Id";
    private RadioGroup radioGroup;

    public static void goJianResultPublishActivity(Context context, String postId){
        Intent intent = new Intent(context, JianResultPublishActivity.class);
        intent.putExtra(Post_Id, postId);
        context.startActivity(intent);
    }

    private EditText editContent;
    private ProgressDialog progressDialog;

    private String postId;
    private boolean isTrue = true;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_jian_result_publish;
    }

    @Override
    protected void setupView() {
        postId = getIntent().getStringExtra(Post_Id);

        mTitleTextView.setText(R.string.title_jian_result_publish);

        radioGroup = (RadioGroup)this.findViewById(R.id.radioGroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int radioId) {
                if(radioId == R.id.radioTrue){
                    isTrue = true;
                }else {
                    isTrue = false;
                }
            }
        });
        editContent = (EditText) findViewById(R.id.editContent);


        findViewById(R.id.buttonCommit).setOnClickListener(this);

        initData();
    }

    @Override
    protected void initData() {

    }


    /*点击处理*/
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonCommit:
                publishJianResult();
                break;

        }
    }

    /*提交藏品*/
    private void publishJianResult() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("提交中，请稍等...");
        progressDialog.show();
        App.app.appAction.publishJianResult(postId, isTrue, editContent.getText().toString().trim(),new BaseActionCallbackListener<Void>() {
            @Override
            public void onSuccess(Void data) {
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }
                ToastUtil.showToast(App.app, "鉴定成功！");
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

}



