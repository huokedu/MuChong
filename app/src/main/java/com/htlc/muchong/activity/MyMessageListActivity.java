package com.htlc.muchong.activity;

import android.graphics.Rect;
import android.net.Uri;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.chanven.lib.cptr.PtrClassicFrameLayout;
import com.chanven.lib.cptr.PtrDefaultHandler;
import com.chanven.lib.cptr.PtrFrameLayout;
import com.chanven.lib.cptr.PtrHandler;
import com.chanven.lib.cptr.loadmore.OnLoadMoreListener;
import com.chanven.lib.cptr.recyclerview.RecyclerAdapterWithHF;
import com.htlc.muchong.App;
import com.htlc.muchong.R;
import com.htlc.muchong.adapter.FourthOneRecyclerViewAdapter;
import com.htlc.muchong.adapter.ThirdRecyclerViewAdapter;
import com.htlc.muchong.base.BaseActivity;
import com.htlc.muchong.base.BaseRecyclerViewAdapter;
import com.htlc.muchong.util.DateFormat;
import com.htlc.muchong.util.ImageUtil;
import com.htlc.muchong.util.LoginUtil;
import com.htlc.muchong.util.PersonUtil;
import com.larno.util.CommonUtil;
import com.larno.util.ToastUtil;

import java.util.List;

import api.Api;
import core.AppActionImpl;
import model.MessageBean;
import model.PostBean;
import model.UserBean;

/**
 * Created by sks on 2016/5/23.
 * 个人中心---消息中心
 */
public class MyMessageListActivity extends BaseActivity {
    private PtrClassicFrameLayout mPtrFrame;
    private MyMessageRecyclerViewAdapter adapter;
    private RecyclerAdapterWithHF mAdapter;
    private RecyclerView mRecyclerView;
    private View noDataView;

    int page = 1;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_pai_list;
    }

    @Override
    protected void setupView() {
        mTitleTextView.setText(R.string.fifth_xiao);

        noDataView = findViewById(R.id.noDataView);
        mPtrFrame = (PtrClassicFrameLayout) findViewById(R.id.rotate_header_list_view_frame);
        mPtrFrame.setLastUpdateTimeKey(null);
        mPtrFrame.setPtrHandler(new PtrHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                initData();
            }

            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
            }
        });
        mPtrFrame.postDelayed(new Runnable() {
            @Override
            public void run() {
                mPtrFrame.autoRefresh();
            }
        }, 500);
        mPtrFrame.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void loadMore() {
                loadMoreData();
            }
        });


        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mRecyclerView.setBackgroundResource(R.color.bg_gray);
        adapter = new MyMessageRecyclerViewAdapter();
        mAdapter = new RecyclerAdapterWithHF(adapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            private int space = CommonUtil.dp2px(MyMessageListActivity.this, 10);

            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                outRect.bottom = space;
            }
        });
        adapter.setOnItemClickListener(new ThirdRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                MessageBean bean = adapter.getData().get(position);
                UserBean user = LoginUtil.getUser();
                String url = String.format(Api.MessageDetailHtml, user.id, user.user_token, bean.id);
                WebActivity.goWebActivity(MyMessageListActivity.this, bean.msg_title, url);
            }
        });
        mRecyclerView.setAdapter(mAdapter);

    }

    private void loadMoreData() {
        App.app.appAction.messageList(page, new BaseActionCallbackListener<List<MessageBean>>() {
            @Override
            public void onSuccess(List<MessageBean> data) {
                adapter.setData(data, true);
                if (data.size() < AppActionImpl.PAGE_SIZE) {
                    mPtrFrame.loadMoreComplete(false);
                } else {
                    mPtrFrame.loadMoreComplete(true);
                }
                page++;
            }

            @Override
            public void onIllegalState(String errorEvent, String message) {
                ToastUtil.showToast(App.app, message);
                mPtrFrame.refreshComplete();
                mPtrFrame.setFail();
            }
        });
    }

    @Override
    protected void initData() {
        page = 1;
        App.app.appAction.messageList(page, new BaseActionCallbackListener<List<MessageBean>>() {
            @Override
            public void onSuccess(List<MessageBean> data) {
                mPtrFrame.refreshComplete();
                adapter.setData(data, false);
                if (data.size() < AppActionImpl.PAGE_SIZE) {
                    mPtrFrame.setLoadMoreEnable(false);
                } else {
                    mPtrFrame.setLoadMoreEnable(true);
                }
                page++;
                showOrHiddenNoDataView(adapter.getData(), noDataView);
            }

            @Override
            public void onIllegalState(String errorEvent, String message) {
                ToastUtil.showToast(App.app, message);
                mPtrFrame.refreshComplete();
                mPtrFrame.setLoadMoreEnable(false);
                showOrHiddenNoDataView(adapter.getData(), noDataView);
            }
        });
    }

    /*删除消息*/
    private void deleteMessage(String msgId) {
        App.app.appAction.deleteMessage(msgId, new BaseActionCallbackListener<Void>() {
            @Override
            public void onSuccess(Void data) {
                ToastUtil.showToast(App.app, "删除成功");
                initData();
            }

            @Override
            public void onIllegalState(String errorEvent, String message) {
                ToastUtil.showToast(App.app, message);
            }
        });
    }

    private class MyMessageRecyclerViewAdapter extends BaseRecyclerViewAdapter<MessageBean> {
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = View.inflate(parent.getContext(), R.layout.adapter_my_message, null);
            ViewHolder vh = new ViewHolder(view);
            return vh;

        }

        @Override
        public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
            super.onBindViewHolder(holder, position);
            ViewHolder viewHolder = (ViewHolder) holder;
            final MessageBean bean = mList.get(position);
            ImageUtil.setImageByDefault(viewHolder.imageView, R.mipmap.default_third_gird_head, Uri.parse(bean.msg_coverimg));
            DateFormat.setTextByTime(viewHolder.textTime, bean.msg_ctime);
            viewHolder.textTitle.setText(bean.msg_title);
            viewHolder.textDescription.setText(bean.msg_content);
            viewHolder.imageDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deleteMessage(bean.id);
                }
            });
            viewHolder.imageFlag.setVisibility(bean.isread.equals("1") ? View.VISIBLE : View.INVISIBLE);


        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public ImageView imageView, imageFlag, imageDelete;
            public TextView textTime, textTitle, textDescription;

            public ViewHolder(View view) {
                super(view);
                imageView = (ImageView) view.findViewById(R.id.imageView);
                imageFlag = (ImageView) view.findViewById(R.id.imageFlag);
                imageDelete = (ImageView) view.findViewById(R.id.imageDelete);
                textTime = (TextView) view.findViewById(R.id.textTime);
                textTitle = (TextView) view.findViewById(R.id.textTitle);
                textDescription = (TextView) view.findViewById(R.id.textDescription);
            }
        }
    }
}
