package com.htlc.muchong.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Rect;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chanven.lib.cptr.PtrClassicFrameLayout;
import com.chanven.lib.cptr.PtrDefaultHandler;
import com.chanven.lib.cptr.PtrFrameLayout;
import com.chanven.lib.cptr.PtrHandler;
import com.chanven.lib.cptr.loadmore.OnLoadMoreListener;
import com.chanven.lib.cptr.recyclerview.RecyclerAdapterWithHF;
import com.htlc.muchong.App;
import com.htlc.muchong.R;
import com.htlc.muchong.adapter.CangPersonRecyclerViewAdapter;
import com.htlc.muchong.adapter.FirstRecyclerViewAdapter;
import com.htlc.muchong.adapter.FourthFourRecyclerViewAdapter;
import com.htlc.muchong.adapter.FourthTwoRecyclerViewAdapter;
import com.htlc.muchong.adapter.JianRecyclerViewAdapter;
import com.htlc.muchong.adapter.PaiRecyclerViewAdapter;
import com.htlc.muchong.adapter.ThirdRecyclerViewAdapter;
import com.htlc.muchong.base.BaseActivity;
import com.htlc.muchong.base.BaseRecyclerViewAdapter;
import com.htlc.muchong.fragment.JianListFragment;
import com.htlc.muchong.fragment.SecondFragment;
import com.htlc.muchong.util.LoginUtil;
import com.larno.util.CommonUtil;
import com.larno.util.ToastUtil;

import java.util.Arrays;
import java.util.List;

import core.AppActionImpl;
import model.GoodsBean;
import model.JianBean;
import model.PersonBean;
import model.SchoolBean;

/**
 * Created by sks on 2016/5/24.
 */
public class TaLikeActivity extends BaseActivity {
    public static final String Person_Id = "Person_Id";
    private int page = 1;
    private RecyclerView.ItemDecoration decor;

    public static void goTaLikeActivity(Context context, String personId){
        Intent intent = new Intent(context, TaLikeActivity.class);
        intent.putExtra(Person_Id, personId);
        context.startActivity(intent);
    }

    private PtrClassicFrameLayout mPtrFrame;
    private BaseRecyclerViewAdapter adapter;
    private RecyclerAdapterWithHF mAdapter;
    private RecyclerView mRecyclerView;

    private int currentType = 0;
    private String personId;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_ta_like_list;
    }

    @Override
    protected void setupView() {
        personId = getIntent().getStringExtra(Person_Id);

        mTitleRightTextView.setText(R.string.like_type);
        mTitleRightTextView.setVisibility(View.VISIBLE);
        mTitleRightTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectLikeType();
            }
        });

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
        mPtrFrame.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void loadMore() {
                loadMoreData();
            }
        });

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        setTitle();
    }

    private void setTitle() {
        if(LoginUtil.getUser().id.equals(personId)){
            mTitleTextView.setText(String.format(getString(R.string.title_my_like), getResources().getStringArray(R.array.like_type_array)[currentType]));
        }else {
            mTitleTextView.setText(String.format(getString(R.string.title_ta_like), getResources().getStringArray(R.array.like_type_array)[currentType]));
        }
        mRecyclerView.removeAllViews();
        mRecyclerView.removeItemDecoration(decor);
        //商品
        if(currentType == 0){
            mRecyclerView.setBackgroundResource(android.R.color.white);
            adapter = new FirstRecyclerViewAdapter();
            mAdapter = new RecyclerAdapterWithHF(adapter);
            mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2) {
            });
            decor = new RecyclerView.ItemDecoration() {
                private int space = CommonUtil.dp2px(TaLikeActivity.this, 10);

                @Override
                public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                    if (parent.getChildAdapterPosition(view) % 2 == 0) {
                        outRect.bottom = space;
                        outRect.left = space;
                        outRect.right = space / 2;
                    } else {
                        outRect.bottom = space;
                        outRect.right = space;
                        outRect.left = space / 2;
                    }
                    if (parent.getChildAdapterPosition(view) < 2) {
                        outRect.top = space;
                    }

                }
            };
            mRecyclerView.addItemDecoration(decor);

            mRecyclerView.setAdapter(mAdapter);
            adapter.setOnItemClickListener(new BaseRecyclerViewAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    GoodsBean bean = (GoodsBean) adapter.getData().get(position);
                    ProductDetailActivity.goProductActivity(TaLikeActivity.this, bean.id);
                }
            });
            //藏品
        }else if(currentType == 1){
            mRecyclerView.setBackgroundResource(R.color.bg_gray);
            adapter = new CangPersonRecyclerViewAdapter();
            mAdapter = new RecyclerAdapterWithHF(adapter);
            mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2) {
            });
            decor = new RecyclerView.ItemDecoration() {
                private int space = CommonUtil.dp2px(TaLikeActivity.this, 10);

                @Override
                public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                    if (parent.getChildAdapterPosition(view) % 2 == 0) {
                        outRect.bottom = space;
                        outRect.left = space;
                        outRect.right = space / 2;
                    } else {
                        outRect.bottom = space;
                        outRect.right = space;
                        outRect.left = space / 2;
                    }
                    if (parent.getChildAdapterPosition(view) < 2) {
                        outRect.top = space;
                    }

                }
            };
            mRecyclerView.addItemDecoration(decor);

            mRecyclerView.setAdapter(mAdapter);
            adapter.setOnItemClickListener(new ThirdRecyclerViewAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    JianBean bean = (JianBean) adapter.getData().get(position);
                    CangDetailActivity.goCangDetailActivity(TaLikeActivity.this, bean.id, R.string.title_cang_detail);

                }
            });
            //学堂
        }else if(currentType == 2){
            mRecyclerView.setBackgroundResource(R.color.bg_gray);
            adapter = new FourthFourRecyclerViewAdapter();
            mAdapter = new RecyclerAdapterWithHF(adapter);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(TaLikeActivity.this));
            decor = new RecyclerView.ItemDecoration() {
                private int space = CommonUtil.dp2px(TaLikeActivity.this, 10);

                @Override
                public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                    outRect.bottom = space;
                }
            };
            mRecyclerView.addItemDecoration(decor);

            mRecyclerView.setAdapter(mAdapter);
            adapter.setOnItemClickListener(new ThirdRecyclerViewAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    SchoolBean bean = (SchoolBean) adapter.getData().get(position);
                    PostDetailActivity.goPostDetailActivity(TaLikeActivity.this, bean.id, R.string.detail, true);
                }
            });
            //鉴定
        }else if(currentType == 3){
            mRecyclerView.setBackgroundResource(R.color.bg_gray);
            adapter = new JianRecyclerViewAdapter();
            mAdapter = new RecyclerAdapterWithHF(adapter);
            mRecyclerView.setLayoutManager(new GridLayoutManager(TaLikeActivity.this, 2) {
            });
            decor = new RecyclerView.ItemDecoration() {
                private int space = CommonUtil.dp2px(TaLikeActivity.this, 10);

                @Override
                public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                    if (parent.getChildAdapterPosition(view) % 2 == 0) {
                        outRect.bottom = space;
                        outRect.left = space;
                        outRect.right = space / 2;
                    } else {
                        outRect.bottom = space;
                        outRect.right = space;
                        outRect.left = space / 2;
                    }
                    if (parent.getChildAdapterPosition(view) < 2) {
                        outRect.top = space;
                    }
                }
            };
            mRecyclerView.addItemDecoration(decor);

            mRecyclerView.setAdapter(mAdapter);
            adapter.setOnItemClickListener(new BaseRecyclerViewAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    JianBean bean = (JianBean) adapter.getData().get(position);
                    JianDetailActivity.goJianDetailActivity(TaLikeActivity.this, bean.id, !bean.forum_yesorno.equals(JianListFragment.TYPE_3));
                }
            });
            //藏家
        }else if(currentType == 4){
            mRecyclerView.setBackgroundResource(R.color.bg_gray);
            adapter = new FourthTwoRecyclerViewAdapter();
            mAdapter = new RecyclerAdapterWithHF(adapter);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(TaLikeActivity.this));

            mRecyclerView.setAdapter(mAdapter);
            adapter.setOnItemClickListener(new ThirdRecyclerViewAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    PersonBean bean = (PersonBean) adapter.getData().get(position);
                    PersonActivity.goPersonActivity(TaLikeActivity.this, bean.id);
                }
            });
        }

        mPtrFrame.postDelayed(new Runnable() {
            @Override
            public void run() {
                mPtrFrame.autoRefresh();
            }
        }, 500);
    }

    private void selectLikeType() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this,R.style.DialogAppCompat);
        builder.setItems(R.array.like_type_array, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                if(which!=currentType){
                    currentType = which;
                    setTitle();
                }
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void loadMoreData() {
        if(currentType == 0){
            App.app.appAction.likeListByTypeOfProduct(page, personId, new BaseActionCallbackListener<List<GoodsBean>>() {
                @Override
                public void onSuccess(List<GoodsBean> data) {

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
        }else  if(currentType == 1){
            App.app.appAction.cangListByPersonId(page, personId, new BaseActionCallbackListener<List<JianBean>>() {
                @Override
                public void onSuccess(List<JianBean> data) {

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
        }else if(currentType == 2){
            App.app.appAction.likeListByTypeOfSchool(page, personId, new BaseActionCallbackListener<List<SchoolBean>>() {
                 @Override
                 public void onSuccess(List<SchoolBean> data) {
                     mPtrFrame.loadMoreComplete(true);
                     adapter.setData(data, true);
                     if (data.size() < AppActionImpl.PAGE_SIZE) {
                         mPtrFrame.setNoMoreData();
                     } else {
                         mPtrFrame.setLoadMoreEnable(true);
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
        }else if(currentType == 3){
            App.app.appAction.likeListByTypeOfJian(page, personId, new BaseActionCallbackListener<List<JianBean>>() {
                @Override
                public void onSuccess(List<JianBean> data) {
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
        }else if(currentType == 4){
            App.app.appAction.likeListByTypeOfPerson(page, personId, new BaseActionCallbackListener<List<PersonBean>>() {
                @Override
                public void onSuccess(List<PersonBean> data) {
                    mPtrFrame.loadMoreComplete(true);
                    adapter.setData(data, true);
                    if (data.size() < AppActionImpl.PAGE_SIZE) {
                        mPtrFrame.setNoMoreData();
                    } else {
                        mPtrFrame.setLoadMoreEnable(true);
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
    }

    @Override
    protected void initData() {
        if(currentType == 0){
            page = 1;
            App.app.appAction.likeListByTypeOfProduct(page, personId, new BaseActionCallbackListener<List<GoodsBean>>() {
                @Override
                public void onSuccess(List<GoodsBean> data) {
                    mPtrFrame.refreshComplete();
                    adapter.setData(data, false);
                    if (data.size() < AppActionImpl.PAGE_SIZE) {
                        mPtrFrame.setLoadMoreEnable(false);
                    } else {
                        mPtrFrame.setLoadMoreEnable(true);
                    }
                    page++;
                }

                @Override
                public void onIllegalState(String errorEvent, String message) {
                    ToastUtil.showToast(App.app, message);
                    mPtrFrame.refreshComplete();
                    mPtrFrame.setLoadMoreEnable(false);
                }
            });
        }else  if(currentType == 1){
            page = 1;
            App.app.appAction.likeListByTypeOfCang(page, personId, new BaseActionCallbackListener<List<JianBean>>() {
                @Override
                public void onSuccess(List<JianBean> data) {
                    mPtrFrame.refreshComplete();
                    adapter.setData(data, false);
                    if (data.size() < AppActionImpl.PAGE_SIZE) {
                        mPtrFrame.setLoadMoreEnable(false);
                    } else {
                        mPtrFrame.setLoadMoreEnable(true);
                    }
                    page++;
                }

                @Override
                public void onIllegalState(String errorEvent, String message) {
                    ToastUtil.showToast(App.app, message);
                    mPtrFrame.refreshComplete();
                    mPtrFrame.setLoadMoreEnable(false);
                }
            });
        }else if(currentType == 2){
            page = 1;
            App.app.appAction.likeListByTypeOfSchool(page, personId, new BaseActionCallbackListener<List<SchoolBean>>() {
                @Override
                public void onSuccess(List<SchoolBean> data) {
                    mPtrFrame.refreshComplete();
                    adapter.setData(data, false);
                    if (data.size() < AppActionImpl.PAGE_SIZE) {
                        mPtrFrame.setLoadMoreEnable(false);
                    } else {
                        mPtrFrame.setLoadMoreEnable(true);
                    }
                    page++;
                }

                @Override
                public void onIllegalState(String errorEvent, String message) {
                    ToastUtil.showToast(App.app, message);
                    mPtrFrame.refreshComplete();
                    mPtrFrame.setLoadMoreEnable(false);
                }
            });
        }else if(currentType == 3){
            page = 1;
            App.app.appAction.likeListByTypeOfJian(page, personId, new BaseActionCallbackListener<List<JianBean>>() {
                @Override
                public void onSuccess(List<JianBean> data) {
                    mPtrFrame.refreshComplete();
                    adapter.setData(data, false);
                    if (data.size() < AppActionImpl.PAGE_SIZE) {
                        mPtrFrame.setLoadMoreEnable(false);
                    } else {
                        mPtrFrame.setLoadMoreEnable(true);
                    }
                    page++;
                }

                @Override
                public void onIllegalState(String errorEvent, String message) {
                    ToastUtil.showToast(App.app, message);
                    mPtrFrame.refreshComplete();
                    mPtrFrame.setLoadMoreEnable(false);
                }
            });
        }else if(currentType == 4){
            page = 1;
            App.app.appAction.likeListByTypeOfPerson(page, personId, new BaseActionCallbackListener<List<PersonBean>>() {
                @Override
                public void onSuccess(List<PersonBean> data) {
                    mPtrFrame.refreshComplete();
                    adapter.setData(data, false);
                    if (data.size() < AppActionImpl.PAGE_SIZE) {
                        mPtrFrame.setLoadMoreEnable(false);
                    } else {
                        mPtrFrame.setLoadMoreEnable(true);
                    }
                    page++;
                }

                @Override
                public void onIllegalState(String errorEvent, String message) {
                    ToastUtil.showToast(App.app, message);
                    mPtrFrame.refreshComplete();
                    mPtrFrame.setLoadMoreEnable(false);
                }
            });
        }
    }
}
