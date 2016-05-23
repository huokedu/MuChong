package com.htlc.muchong.activity;

import android.content.Intent;
import android.text.Editable;
import android.view.View;
import android.widget.EditText;

import com.htlc.muchong.R;
import com.htlc.muchong.base.BaseActivity;

/**
 * Created by sks on 2016/5/21.
 */
public class LoginActivity extends BaseActivity{
    private EditText editUsername,editPassword;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected void setupView() {
        mTitleTextView.setText(R.string.title_login);
        findViewById(R.id.textRegister).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
//                String string = mTitleTextView.getText().toString();
//                char[] chars = mTitleTextView.getText().toString().toCharArray();
//                int i = Character.toCodePoint(chars[0], chars[1]);
//                editUsername.setText(chars,0,chars.length);
//                editUsername.setText(string);
            }
        });

        editUsername = (EditText) findViewById(R.id.editUsername);
//        int unicodeJoy = 0x1F602;
//        String emojiString = getEmojiStringByUnicode(unicodeJoy);
//        emojiString = emojiString + "人";
//        mTitleTextView.setText(emojiString);

        editPassword = (EditText) findViewById(R.id.editPassword);
    }
    private String getEmojiStringByUnicode(int unicode){
        return new String(Character.toChars(unicode));
    }
    @Override
    protected void initData() {

    }
}
