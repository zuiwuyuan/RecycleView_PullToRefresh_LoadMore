package com.lnyp.fastrecyclerview.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lnyp.fastrecyclerview.R;
import com.lnyp.fastrecyclerview.bean.WeChatModel;

import java.util.List;

/**
 * 微信精选列表
 */
public class WeChatListAdapter extends RecyclerView.Adapter {

    private LayoutInflater mLayoutInflater;
    private List<WeChatModel> weChatModels;

    private View.OnClickListener wechatItemClick;

    private Context mContext;

    public WeChatListAdapter(Context context, List<WeChatModel> weChatModels, View.OnClickListener wechatItemClick) {

        this.mContext = context;

        this.mLayoutInflater = LayoutInflater.from(context);

        this.weChatModels = weChatModels;

        this.wechatItemClick = wechatItemClick;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(mLayoutInflater.inflate(R.layout.list_item_wechat, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        ViewHolder viewHolder = (ViewHolder) holder;

        WeChatModel weChatModel = weChatModels.get(position);

        if (weChatModel != null) {

            try {

                Glide.with(mContext).load(weChatModel.getFirstImg()).placeholder(R.drawable.empty_default_img).into(viewHolder.imgWeChat);

                viewHolder.textTitle.setText(weChatModel.getTitle());

                viewHolder.textSource.setText("来源 ：" + weChatModel.getSource());

                viewHolder.itemView.setTag(position);
                viewHolder.itemView.setOnClickListener(wechatItemClick);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public int getItemCount() {
        return weChatModels.size();
    }

    private class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView imgWeChat;

        private TextView textTitle;

        private TextView textSource;

        public ViewHolder(View itemView) {
            super(itemView);

            imgWeChat = (ImageView) itemView.findViewById(R.id.imgWeChat);
            textTitle = (TextView) itemView.findViewById(R.id.textTitle);
            textSource = (TextView) itemView.findViewById(R.id.textSource);
        }
    }
}
