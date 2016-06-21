package com.htlc.muchong.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.htlc.muchong.App;
import com.htlc.muchong.R;
import com.htlc.muchong.adapter.DetailImageAdapter;
import com.htlc.muchong.adapter.JianResultAdapter;
import com.htlc.muchong.adapter.PostCommentAdapter;
import com.htlc.muchong.adapter.ProfessorCommentAdapter;
import com.htlc.muchong.base.BaseActivity;
import com.htlc.muchong.fragment.JianListFragment;
import com.htlc.muchong.util.DateFormat;
import com.htlc.muchong.util.ImageUtil;
import com.htlc.muchong.util.LoginUtil;
import com.htlc.muchong.util.PersonUtil;
import com.htlc.muchong.widget.LoadMoreScrollView;
import com.larno.util.ToastUtil;

import java.util.Arrays;
import java.util.List;

import core.AppActionImpl;
import model.PostCommentBean;
import model.PostDetailBean;
import model.UserBean;

/**
 * Created by sks on 2016/5/24.
 */
public class JianDetailActivity extends BaseActivity implements View.OnClickListener {
    private static final String Post_Id = "Post_Id";
    private static final String Is_Over = "Is_Over";


    public static void goJianDetailActivity(Context context, String postId, boolean isOver) {
        Intent intent = new Intent(context, JianDetailActivity.class);
        intent.putExtra(Post_Id, postId);
        intent.putExtra(Is_Over, isOver);
        context.startActivity(intent);
    }

    private ListView imageListView, resultListView, mProfessorCommentListView, mCommentListView;
    private DetailImageAdapter imageAdapter;
    private JianResultAdapter jianResultAdapter;
    private PostCommentAdapter commentAdapter;
    private ProfessorCommentAdapter professorCommentAdapter;
    private TextView textResult;

    private ImageView imageHead;
    private TextView textName;
    private TextView textLevel;
    private TextView textTime;

    private TextView textContent;

    private TextView textComment;
    private TextView textCommentMore;

    private LoadMoreScrollView scrollView;
    private RelativeLayout relativeInput;
    private Button textButton;
    private EditText editComment;

    private String postId;
    private boolean isOver;
    private boolean hasMore = false;
    private boolean isLoading = true;
    private int page = 2;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_jian_detail;
    }

    @Override
    protected void setupView() {
        postId = getIntent().getStringExtra(Post_Id);
        isOver = getIntent().getBooleanExtra(Is_Over,false);
        mTitleTextView.setText(R.string.title_jian_detail);
        mTitleRightTextView.setText(R.string.jian_detail_jian);
        mTitleRightTextView.setVisibility(!isOver && App.app.isLogin() && LoginUtil.getUser().user_role.equals(UserBean.TYPE_EXPERT) ? View.VISIBLE:View.INVISIBLE);
        mTitleRightTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(App.app.isLogin()){
                    JianResultPublishActivity.goJianResultPublishActivity(JianDetailActivity.this,postId);
                }else {
                    LoginUtil.showLoginTips(JianDetailActivity.this);
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
//作者信息
        imageHead = (ImageView) findViewById(R.id.imageHead);
        textName = (TextView) findViewById(R.id.textName);
        textLevel = (TextView) findViewById(R.id.textLevel);
        textTime = (TextView) findViewById(R.id.textTime);
//文章详情
        textContent = (TextView) findViewById(R.id.textContent);
        imageListView = (ListView) findViewById(R.id.imageListView);
        imageAdapter = new DetailImageAdapter();
        imageListView.setAdapter(imageAdapter);
//鉴定结果
        textResult = (TextView) findViewById(R.id.textResult);
        resultListView = (ListView) findViewById(R.id.resultListView);
        jianResultAdapter = new JianResultAdapter();
        resultListView.setAdapter(jianResultAdapter);
//鉴定评论
        mProfessorCommentListView = (ListView) findViewById(R.id.professorCommentListView);
        professorCommentAdapter = new ProfessorCommentAdapter();
        mProfessorCommentListView.setAdapter(professorCommentAdapter);
//评论列表
        textComment = (TextView) findViewById(R.id.textComment);
        textCommentMore = (TextView) findViewById(R.id.textCommentMore);
        mCommentListView = (ListView) findViewById(R.id.commentListView);
        commentAdapter = new PostCommentAdapter();
        mCommentListView.setAdapter(commentAdapter);
//输入
        relativeInput = (RelativeLayout) findViewById(R.id.relativeInput);
        textButton = (Button) findViewById(R.id.textButton);
        editComment = (EditText) findViewById(R.id.editComment);
        textButton.setOnClickListener(this);

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
        App.app.appAction.postDetail(postId, new BaseActionCallbackListener<PostDetailBean>() {
            @Override
            public void onSuccess(PostDetailBean data) {
                //作者
                ImageUtil.setCircleImageByDefault(imageHead, R.mipmap.default_third_gird_head, Uri.parse(data.userinfo_headportrait));
                textName.setText(data.userinfo_nickname);
                PersonUtil.setPersonLevel(textLevel, data.userinfo_grade);
                DateFormat.setTextByTime(textTime, data.forum_ctime);
                //文章详情
                String[] images = data.forum_imgstr.split(ProductDetailActivity.SPLIT_FLAG);
                imageAdapter.setData(Arrays.asList(images), false);
                textContent.setText(data.forum_content);
                //专家鉴定
                setResultByType(textResult,data.forum_yesorno);
                jianResultAdapter.setData(data.appraisal, false);
                professorCommentAdapter.setData(data.appraisal,false);

                //评论
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

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.textButton:
                commitComment();
                break;
        }
    }

    /*提交评论*/
    private void commitComment() {
        App.app.appAction.addPostComment(postId, editComment.getText().toString().trim(), new BaseActionCallbackListener<Void>() {
            @Override
            public void onSuccess(Void data) {
                ToastUtil.showToast(App.app, "评论成功");
                editComment.setText("");
                initData();
            }

            @Override
            public void onIllegalState(String errorEvent, String message) {
                ToastUtil.showToast(App.app, message);
            }
        });
    }
    /*设置鉴定结果*/
    private void setResultByType(TextView textView, String type){
        textView.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0);
        if(JianListFragment.TYPE_1.equals(type)){
//            textView.setText(getString(R.string.jian_detail_result));
            textView.setCompoundDrawablesWithIntrinsicBounds(0,0,R.mipmap.icon_jian_result,0);
        }else  if(JianListFragment.TYPE_2.equals(type)){
//            textView.setText(getString(R.string.jian_detail_result));
            textView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.icon_jian_result_false, 0);
        }else  if(JianListFragment.TYPE_3.equals(type)){
//            textView.setText(getString(R.string.jian_detail_result));
            textView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.icon_jian_result_unfinish, 0);
        }
    }
}
