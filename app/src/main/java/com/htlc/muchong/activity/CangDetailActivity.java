package com.htlc.muchong.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.htlc.muchong.App;
import com.htlc.muchong.R;
import com.htlc.muchong.adapter.DetailImageAdapter;
import com.htlc.muchong.adapter.PostCommentAdapter;
import com.htlc.muchong.base.BaseActivity;
import com.htlc.muchong.util.DateFormat;
import com.htlc.muchong.util.ImageUtil;
import com.htlc.muchong.util.LoginUtil;
import com.htlc.muchong.util.PersonUtil;
import com.htlc.muchong.util.SoftInputUtil;
import com.htlc.muchong.widget.LoadMoreScrollView;
import com.larno.util.ToastUtil;

import java.util.Arrays;
import java.util.List;

import core.AppActionImpl;
import model.PostCommentBean;
import model.PostDetailBean;

/**
 * Created by sks on 2016/5/24.
 * 藏品专区详情Activity
 */
public class CangDetailActivity extends BaseActivity implements View.OnClickListener {
    public static final String Post_Id = "Post_Id";
    private ImageView imageHead;//用户头像
    private TextView textName;//用户名字
    private TextView textLevel;//用户等级
    private TextView textTime;//发布时间
    private TextView textPostTitle;//帖子标题
    private TextView textContent;//帖子内容
    private TextView textComment;//帖子评论标签，显示评论总数
    private TextView textCommentMore;//查看评论列表

    private LoadMoreScrollView scrollView;
    private RelativeLayout relativeInput;//评论输入view
    private Button textButton;//发布评论按钮
    private EditText editComment;//评论输入编辑框
    private TextView textCommentButton;//评论按钮，点击显示评论输入view
    private TextView textLikeButton;//喜欢按钮
    private LinearLayout linearBottom;

    public static void goCangDetailActivity(Context context, String id, int titleId) {
        Intent intent = new Intent(context, CangDetailActivity.class);
        intent.putExtra(BaseActivity.ActivityTitleId, titleId);
        intent.putExtra(Post_Id, id);
        context.startActivity(intent);
    }

    private ListView imageListView, mCommentListView;//帖子内容图片列表，帖子最近评论列表
    private DetailImageAdapter imageAdapter;
    private PostCommentAdapter commentAdapter;

    private String postId;//当前帖子id
    private boolean hasMore = false;
    private boolean isLoading = true;
    private int page = 2;
    private boolean isLike;//查看帖子的用户，是否喜欢该帖子
    private String reply = "";//当前要回复人的昵称

    @Override
    protected int getLayoutId() {
        return R.layout.activity_cang_detail;
    }

    @Override
    protected void setupView() {
        postId = getIntent().getStringExtra(Post_Id);
        int activityTitleId = getIntent().getIntExtra(ActivityTitleId, 0);
        mTitleTextView.setText(activityTitleId);
        mTitleRightTextView.setBackgroundResource(R.mipmap.icon_share);
        mTitleRightTextView.setVisibility(View.INVISIBLE);
        mTitleRightTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtil.showToast(App.app, "share。。。。。。。。。。");
            }
        });

        scrollView = (LoadMoreScrollView) findViewById(R.id.scrollView);
        scrollView.setOnScrollChangedListener(new LoadMoreScrollView.OnScrollChangedListener() {
            @Override
            public void onScrollChanged(int l, int t, int oldl, int oldt) {

            }

            @Override
            public void onScrollTop() {

            }

            //当Scrollview滚动到底部，加载第2，3....页的评论
            @Override
            public void onScrollBottom() {
                if (hasMore && !isLoading) {
                    loadMoreData();
                }
            }
        });

        imageHead = (ImageView) findViewById(R.id.imageHead);
        textName = (TextView) findViewById(R.id.textName);
        textLevel = (TextView) findViewById(R.id.textLevel);
        textTime = (TextView) findViewById(R.id.textTime);

        textPostTitle = (TextView) findViewById(R.id.textPostTitle);
        textContent = (TextView) findViewById(R.id.textContent);
        imageListView = (ListView) findViewById(R.id.imageListView);
        imageAdapter = new DetailImageAdapter();
        imageListView.setAdapter(imageAdapter);
        imageListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ImageActivity.goImageActivity(CangDetailActivity.this, (String) imageAdapter.getItem(position));

            }
        });


        textComment = (TextView) findViewById(R.id.textComment);
        textCommentMore = (TextView) findViewById(R.id.textCommentMore);
        mCommentListView = (ListView) findViewById(R.id.commentListView);
        mCommentListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PostCommentBean postCommentBean = (PostCommentBean) commentAdapter.getItem(position);
                reply = postCommentBean.userinfo_id;
                editComment.setHint("回复:"+postCommentBean.userinfo_nickname);
                showInput(true);
                SoftInputUtil.showSoftInput(editComment);
            }
        });
        commentAdapter = new PostCommentAdapter();
        mCommentListView.setAdapter(commentAdapter);

        relativeInput = (RelativeLayout) findViewById(R.id.relativeInput);
        textButton = (Button) findViewById(R.id.textButton);
        textButton.setText(R.string.cancel);
        editComment = (EditText) findViewById(R.id.editComment);
        relativeInput.setVisibility(View.GONE);
        textButton.setOnClickListener(this);
        editComment.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().trim().length() < 1) {
                    textButton.setText(R.string.cancel);
                } else {
                    textButton.setText(R.string.comment);
                }
            }
        });

        linearBottom = (LinearLayout) findViewById(R.id.linearBottom);
        textCommentButton = (TextView) findViewById(R.id.textCommentButton);
        textLikeButton = (TextView) findViewById(R.id.textLikeButton);
        textCommentButton.setOnClickListener(this);
        textLikeButton.setOnClickListener(this);

        initData();
    }

    /*获取评论列表*/
    private void loadMoreData() {
        isLoading = true;
        App.app.appAction.postCommentList(postId, page, new BaseActionCallbackListener<List<PostCommentBean>>() {
            @Override
            public void onSuccess(List<PostCommentBean> data) {
                if (data.size() < AppActionImpl.PAGE_SIZE) {
                    hasMore = false;
                } else {
                    hasMore = true;
                }
                commentAdapter.setData(data, true);
                page++;

                isLoading = false;
            }

            @Override
            public void onIllegalState(String errorEvent, String message) {
                ToastUtil.showToast(App.app, message);
                isLoading = false;
            }
        });
    }

    @Override
    protected void initData() {
        isLoading = true;
        App.app.appAction.postDetail(postId, new BaseActionCallbackListener<PostDetailBean>() {
            @Override
            public void onSuccess(PostDetailBean data) {
                ImageUtil.setCircleImageByDefault(imageHead, R.mipmap.default_third_gird_head, Uri.parse(data.userinfo_headportrait));
                textName.setText(data.userinfo_nickname);
                PersonUtil.setPersonLevel(textLevel, data.userinfo_grade);
                DateFormat.setTextByTime(textTime, data.forum_ctime);

                String[] images = data.forum_imgstr.split(ProductDetailActivity.SPLIT_FLAG);
                imageAdapter.setData(Arrays.asList(images), false);
                textPostTitle.setText(data.forum_title);
                textContent.setText(data.forum_content);

                setIsLike("1".equals(data.islike));

                textComment.setText(getString(R.string.product_detail_comment, data.evalcount));
                textCommentMore.setVisibility(View.INVISIBLE);
                if (data.evallist.size() < AppActionImpl.PAGE_SIZE) {
                    hasMore = false;
                } else {
                    hasMore = true;
                }
                commentAdapter.setData(data.evallist, false);

                isLoading = false;
            }

            @Override
            public void onIllegalState(String errorEvent, String message) {
                ToastUtil.showToast(App.app, message);
                isLoading = false;
            }
        });
    }

    /*设置当前喜欢状态*/
    public void setIsLike(boolean isLike) {
        this.isLike = isLike;
        textLikeButton.setSelected(isLike);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.textLikeButton:
                addLike();
                break;
            case R.id.textCommentButton:
                showInput(true);
                break;
            case R.id.textButton:
                if (textButton.getText().toString().equals(getString(R.string.cancel))) {
                    showInput(false);
                } else {
                    commitComment();
                }
                break;
        }
    }


    /*提交评论*/
    private void commitComment() {
        if (!App.app.isLogin()) {
            LoginUtil.showLoginTips(this);
            return;
        }
        String comment = editComment.getText().toString().trim();
        if(TextUtils.isEmpty(comment)){
            ToastUtil.showToast(App.app, "评论内容不能为空");
            return;
        }
        App.app.appAction.addPostComment(postId, comment,reply, new BaseActionCallbackListener<Void>() {
            @Override
            public void onSuccess(Void data) {
                ToastUtil.showToast(App.app, "评论成功");
                editComment.setText("");
                reply = "";
                SoftInputUtil.hideSoftInput(editComment);
                initData();
            }

            @Override
            public void onIllegalState(String errorEvent, String message) {
                ToastUtil.showToast(App.app, message);
            }
        });
    }

    /*切换评论输入 与 喜欢的  显示*/
    private void showInput(boolean flag) {
        if (flag) {
            relativeInput.setVisibility(View.VISIBLE);
            linearBottom.setVisibility(View.GONE);
        } else {
            reply = "";
            editComment.setHint("");
            SoftInputUtil.hideSoftInput(editComment);
            relativeInput.setVisibility(View.GONE);
            linearBottom.setVisibility(View.VISIBLE);
        }
    }

    /*添加喜欢*/
    private void addLike() {
        if (!App.app.isLogin()) {
            LoginUtil.showLoginTips(this);
            return;
        }
        App.app.appAction.addLikePost(postId, new BaseActionCallbackListener<Void>() {
            @Override
            public void onSuccess(Void data) {
                setIsLike(!isLike);
            }

            @Override
            public void onIllegalState(String errorEvent, String message) {
                ToastUtil.showToast(App.app, message);
            }
        });
    }
}
