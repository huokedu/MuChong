package com.htlc.muchong.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.htlc.muchong.App;
import com.htlc.muchong.R;
import com.htlc.muchong.activity.LoginActivity;
import com.htlc.muchong.activity.UserActivity;
import com.htlc.muchong.util.CircleTransform;
import com.htlc.muchong.util.ImageUtil;
import com.htlc.muchong.util.LoginUtil;
import com.squareup.picasso.Picasso;

import model.UserInfoBean;

/**
 * Created by sks on 2016/5/20.
 */
public class FifthRecyclerViewAdapter extends RecyclerView.Adapter {
    public static final int TYPE_HEAD = 1;
    public static final int TYPE_BODY = 0;
    public static final int[] iconArray = {R.mipmap.icon_fifth_chong, R.mipmap.icon_fifth_gou, R.mipmap.icon_fifth_shou, R.mipmap.icon_fifth_jian,
            R.mipmap.icon_fifth_jiao, R.mipmap.icon_fifth_jing, R.mipmap.icon_fifth_lun, R.mipmap.icon_fifth_xiao, R.mipmap.icon_fifth_setting};
    public static final int[] nameArray = {R.string.fifth_chong, R.string.fifth_gou, R.string.fifth_shou, R.string.fifth_jian,
            R.string.fifth_jiao, R.string.fifth_jing, R.string.fifth_lun, R.string.fifth_xiao, R.string.fifth_setting};
    protected OnItemClickListener mOnItemClickListener;

    private UserInfoBean userInfoBean;

    public void setUserInfoBean(UserInfoBean userInfoBean) {
        this.userInfoBean = userInfoBean;
        notifyDataSetChanged();
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    @Override
    public int getItemCount() {
        return iconArray.length + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_HEAD;
        } else {
            return TYPE_BODY;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_BODY) {
            View view = View.inflate(parent.getContext(), R.layout.adapter_fragment_fifth, null);
            ViewHolder vh = new ViewHolder(view);
            return vh;
        } else {
            View view = View.inflate(parent.getContext(), R.layout.header_fragment_fifth, null);
            WindowManager wm = (WindowManager) parent.getContext().getSystemService(Context.WINDOW_SERVICE);
            Point point = new Point();
            wm.getDefaultDisplay().getSize(point);//屏幕宽度
            int height = (int) (point.x * 0.467);
            view.setLayoutParams(new RecyclerView.LayoutParams(point.x, height));
            HeadViewHolder headViewHolder = new HeadViewHolder(view);
            return headViewHolder;
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if (holder.getItemViewType() == TYPE_BODY) {
            final int pos = position - 1;
            if (mOnItemClickListener != null) {
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mOnItemClickListener.onItemClick(pos);
                    }
                });
            }
            ViewHolder tempHolder = (ViewHolder) holder;
            tempHolder.imageView.setImageResource(iconArray[pos]);
            tempHolder.textName.setText(nameArray[pos]);
            if (pos == 0) {
                if(userInfoBean!=null){
                    tempHolder.textOther.setText("余额￥" + userInfoBean.userinfo_money);
                }else {
                    tempHolder.textOther.setText("余额￥0");
                }
            } else {
                tempHolder.textOther.setText("");
            }
        } else if (holder.getItemViewType() == TYPE_HEAD) {
            HeadViewHolder tempHolder = (HeadViewHolder) holder;
            if(userInfoBean!=null){
                ImageUtil.setCircleImageByDefault(tempHolder.imageHead, R.mipmap.default_fourth_two_head, Uri.parse(userInfoBean.userinfo_headportrait));
                tempHolder.textName.setText(TextUtils.isEmpty(userInfoBean.userinfo_nickname)? LoginUtil.getUser().user_account : userInfoBean.userinfo_nickname);
                tempHolder.textFans.setText("粉丝  "+userInfoBean.userinfo_likenum);
                tempHolder.ratingBarLevel.setRating(Float.parseFloat(userInfoBean.userinfo_grade));
            }else {
                tempHolder.imageHead.setImageResource(R.mipmap.default_fourth_two_head);
                tempHolder.textName.setText("昵称");
                tempHolder.textFans.setText("粉丝  0");
                tempHolder.ratingBarLevel.setRating(0.0f);
            }
            tempHolder.imageHead.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(App.app.isLogin()){
                        v.getContext().startActivity(new Intent(v.getContext(), UserActivity.class));
                    }else {
                        v.getContext().startActivity(new Intent(v.getContext(), LoginActivity.class));
                    }

                }
            });
        }

    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textName, textOther;
        public ImageView imageView;

        public ViewHolder(View view) {
            super(view);
            textName = (TextView) view.findViewById(R.id.textName);
            textOther = (TextView) view.findViewById(R.id.textOther);
            imageView = (ImageView) view.findViewById(R.id.imageView);
        }
    }

    public static class HeadViewHolder extends RecyclerView.ViewHolder {
        public TextView textName, textFans;
        public ImageView imageHead;
        public RatingBar ratingBarLevel;

        public HeadViewHolder(View view) {
            super(view);
            textName = (TextView) view.findViewById(R.id.textName);
            textFans = (TextView) view.findViewById(R.id.textFans);
            imageHead = (ImageView) view.findViewById(R.id.imageHead);
            ratingBarLevel = (RatingBar) view.findViewById(R.id.ratingBarLevel);
        }
    }
}
