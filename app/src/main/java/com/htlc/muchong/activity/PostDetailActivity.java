package com.htlc.muchong.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
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
import com.htlc.muchong.util.ShareSdkUtil;
import com.htlc.muchong.util.SoftInputUtil;
import com.htlc.muchong.widget.LoadMoreScrollView;
import com.larno.util.ToastUtil;

import java.util.Arrays;
import java.util.List;

import api.Api;
import core.AppActionImpl;
import model.PostCommentBean;
import model.PostDetailBean;

/**
 * Created by sks on 2016/5/24.
 * 主页论坛---论坛详情
 */
public class PostDetailActivity extends BaseActivity implements View.OnClickListener {
    public static final String Post_Id = "Post_Id";
    public static final String Is_School = "Is_School";
    public static final String Is_ShuoShuo = "Is_ShuoShuo";
    private ImageView imageHead;//楼主头像
    private TextView textName;//楼主昵称
    private TextView textLevel;//楼主等级
    private TextView textTime;//发布时间
    private TextView textPostTitle;//帖子标题
    private TextView textContent;//帖子内容
    private TextView textComment;//帖子评论标题（评论总数）
    private TextView textCommentMore;//查看更多评论

    private LoadMoreScrollView scrollView;
    private Button textButton;//评论按钮
    private EditText editComment;//评论编辑
    private TextView textLike;//喜欢按钮

    private String reply = "";//当前要回复人的昵称

    /*不是学堂的帖子*/
    public static void goPostDetailActivity(Context context, String id, int titleId) {
        Intent intent = new Intent(context, PostDetailActivity.class);
        intent.putExtra(BaseActivity.ActivityTitleId, titleId);
        intent.putExtra(Post_Id, id);
        context.startActivity(intent);
    }

    /*学堂帖子 isSchool为true*/
    public static void goPostDetailActivity(Context context, String id, int titleId, boolean isSchool) {
        Intent intent = new Intent(context, PostDetailActivity.class);
        intent.putExtra(BaseActivity.ActivityTitleId, titleId);
        intent.putExtra(Post_Id, id);
        intent.putExtra(Is_School, isSchool);
        context.startActivity(intent);
    }
    /*说说帖子 isShuoShuo*/
    public static void goPostDetailActivityByShuoShuo(Context context, String id, int titleId, boolean isShuoShuo) {
        Intent intent = new Intent(context, PostDetailActivity.class);
        intent.putExtra(BaseActivity.ActivityTitleId, titleId);
        intent.putExtra(Post_Id, id);
        intent.putExtra(Is_ShuoShuo, isShuoShuo);
        context.startActivity(intent);
    }

    private ListView imageListView, mCommentListView;//图片和评论list view
    private DetailImageAdapter imageAdapter;//图片Adapter
    private PostCommentAdapter commentAdapter;//评论Adapter

    private String postId;//帖子id
    private boolean hasMore = false;
    private boolean isLoading = true;
    private int page = 2;
    private PostDetailBean data;//帖子详情数据
    private boolean isSchool;//帖子是否是school类型
    private boolean isShuoShuo;//帖子是否是shuoShuo类型
    private boolean isLike;//当前用户是否喜欢该帖子，喜欢true;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_post_detail;
    }

    @Override
    protected void setupView() {
        postId = getIntent().getStringExtra(Post_Id);
        isSchool = getIntent().getBooleanExtra(Is_School, false);
        isShuoShuo = getIntent().getBooleanExtra(Is_ShuoShuo, false);
        int activityTitleId = getIntent().getIntExtra(ActivityTitleId, 0);
        mTitleTextView.setText(activityTitleId);
        mTitleRightTextView.setBackgroundResource(R.mipmap.icon_share);
        mTitleRightTextView.setVisibility(View.VISIBLE);
        mTitleRightTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (data != null) {
                    ShareSdkUtil.shareByShareSDK(PostDetailActivity.this, textPostTitle.getText().toString(), textContent.getText().toString(), String.format(Api.SharePostUrl, postId), data.forum_coverimg);
                }
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
                ImageActivity.goImageActivity(PostDetailActivity.this, (String) imageAdapter.getItem(position));

            }
        });
        //是说说，没有标题
        if(isShuoShuo){
            textPostTitle.setVisibility(View.GONE);
        }
        //如果帖子类型是学堂，显示喜欢按钮，否则隐藏;内容按钮GONE
        textLike = (TextView) findViewById(R.id.textLike);
        textLike.setOnClickListener(this);
        if (isSchool) {
            textLike.setVisibility(View.VISIBLE);
            textContent.setVisibility(View.GONE);
        } else {
            textLike.setVisibility(View.INVISIBLE);
        }

        textComment = (TextView) findViewById(R.id.textComment);
        textCommentMore = (TextView) findViewById(R.id.textCommentMore);
        mCommentListView = (ListView) findViewById(R.id.commentListView);
        commentAdapter = new PostCommentAdapter();
        mCommentListView.setAdapter(commentAdapter);
        mCommentListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PostCommentBean postCommentBean = (PostCommentBean) commentAdapter.getItem(position);
                reply = postCommentBean.userinfo_id;
                editComment.setHint("回复:"+postCommentBean.userinfo_nickname);
                showInput(true);
            }
        });

        textButton = (Button) findViewById(R.id.textButton);
        editComment = (EditText) findViewById(R.id.editComment);
        textButton.setText(R.string.cancel);
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
        textButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (textButton.getText().toString().equals(getString(R.string.cancel))) {
                    showInput(false);
                } else {
                    commitComment();
                }
            }
        });

        initData();
    }

    /*显示输入框与隐藏输入框*/
    private void showInput(boolean flag) {
        if (flag) {
            SoftInputUtil.showSoftInput(editComment);
        } else {
            reply = "";
            editComment.setHint("");
            editComment.setText("");
            SoftInputUtil.hideSoftInput(editComment);
        }
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
                PostDetailActivity.this.data = data;
                ImageUtil.setCircleImageByDefault(imageHead, R.mipmap.default_third_gird_head, Uri.parse(data.userinfo_headportrait));
                textName.setText(data.userinfo_nickname);
                PersonUtil.setPersonLevel(textLevel, data.userinfo_grade);
                DateFormat.setTextByTime(textTime, data.forum_ctime);

                String[] images = data.forum_imgstr.split(ProductDetailActivity.SPLIT_FLAG);
                if(isSchool){
                    imageAdapter.setContentJsonArrayStr(data.forum_content);
                }
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.textButton:
                commitComment();
                break;
            case R.id.textLike:
                addLike();
                break;
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

    /*设置当前喜欢状态*/
    public void setIsLike(boolean isLike) {
        this.isLike = isLike;
        textLike.setText(isLike ? R.string.un_collect : R.string.collect);
    }

    /*提交评论*/
    private void commitComment() {
        if(!App.app.isLogin()){
            LoginUtil.showLoginTips(this);
            return;
        }
        App.app.appAction.addPostComment(postId, editComment.getText().toString().trim(),reply, new BaseActionCallbackListener<Void>() {
            @Override
            public void onSuccess(Void data) {
                ToastUtil.showToast(App.app, "评论成功");
                showInput(false);
                initData();
            }

            @Override
            public void onIllegalState(String errorEvent, String message) {
                ToastUtil.showToast(App.app, message);
            }
        });
    }

}
