package com.lnyp.fastrecyclerview.activities;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lnyp.fastrecyclerview.R;
import com.lnyp.fastrecyclerview.adapter.WeChatListAdapter;
import com.lnyp.fastrecyclerview.bean.WeChatModel;
import com.lnyp.fastrecyclerview.http.HttpUtil;
import com.lnyp.fastrecyclerview.http.IOAuthCallBack;
import com.lnyp.fastrecyclerview.resp.RespWeChats;
import com.lnyp.fastrecyclerview.util.GsonUtils;
import com.lnyp.flexibledivider.HorizontalDividerItemDecoration;
import com.lnyp.recyclerview.EndlessRecyclerOnScrollListener;
import com.lnyp.recyclerview.HeaderAndFooterRecyclerViewAdapter;
import com.lnyp.recyclerview.RecyclerViewLoadingFooter;
import com.lnyp.recyclerview.RecyclerViewStateUtils;

import java.util.ArrayList;
import java.util.List;

public class WChatListActivity extends AppCompatActivity {

    private static final String URL = "http://v.juhe.cn/weixin/query";

    private static final int PAGE_SIXE = 10;

    public RecyclerView listWeChats;

    private List<WeChatModel> mDatas;

    private HeaderAndFooterRecyclerViewAdapter recyclerViewAdapter;

    private int pno = 1;

    private boolean hasMore = false;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wchat_list);

        initView();

        progressDialog = new ProgressDialog(this);
        progressDialog.show();

        qrrDataFromServer();
    }

    private void initView() {

        listWeChats = (RecyclerView) findViewById(R.id.listWeChats);

        mDatas = new ArrayList<>();

        WeChatListAdapter weChatListAdapter = new WeChatListAdapter(this, mDatas, onClickListener);
        recyclerViewAdapter = new HeaderAndFooterRecyclerViewAdapter(weChatListAdapter);
        listWeChats.setAdapter(recyclerViewAdapter);

        listWeChats.setLayoutManager(new LinearLayoutManager(this));

        listWeChats.addItemDecoration(
                new HorizontalDividerItemDecoration.Builder(WChatListActivity.this)
                        .color(Color.parseColor("#00c7c0"))
                        .sizeResId(R.dimen.list_divider_height)
                        .showLastDivider()
                        .marginResId(R.dimen.list_divider_left_margin, R.dimen.list_divider_right_margin)
                        .build());

        listWeChats.addOnScrollListener(mOnScrollListener);

    }

    private void qrrDataFromServer() {

        RequestParams requestParams = new RequestParams("UTF-8");
        requestParams.addQueryStringParameter("pno", "" + pno);
        requestParams.addQueryStringParameter("ps", "" + PAGE_SIXE);
        requestParams.addQueryStringParameter("key", "4ea6ce08928d5360747d01bc5246463e");

        HttpUtil.sendRequest(this, HttpRequest.HttpMethod.GET, URL, requestParams, new IOAuthCallBack() {
            @Override
            public void getIOAuthCallBack(int code, String result) {

                switch (code) {
                    case 200:
                        dealData(result);
                        RecyclerViewStateUtils.setFooterViewState(listWeChats, RecyclerViewLoadingFooter.State.Normal);
                        break;
                    case 400:
                        RecyclerViewStateUtils.setFooterViewState(WChatListActivity.this, listWeChats, PAGE_SIXE, RecyclerViewLoadingFooter.State.NetWorkError, mFooterClick);
                        break;
                }

                progressDialog.dismiss();

            }
        });
    }

    private void dealData(String result) {
        try {
            RespWeChats respWeChats = (RespWeChats) GsonUtils.fromJson(result, RespWeChats.class);

            if (0 == respWeChats.getError_code() && respWeChats.getResult() != null) {

                RespWeChats.ResultEntity resultEntity = respWeChats.getResult();
                int totalPage = resultEntity.getTotalPage();
                if (pno < totalPage) {
                    hasMore = true;
                } else {
                    hasMore = false;
                }
                pno = resultEntity.getPno() + 1;

                List<WeChatModel> weChatModels = respWeChats.getResult().getList();

                if (weChatModels != null) {
                    mDatas.addAll(weChatModels);
                }

                updateData();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateData() {

        recyclerViewAdapter.notifyDataSetChanged();
    }

    private EndlessRecyclerOnScrollListener mOnScrollListener = new EndlessRecyclerOnScrollListener() {
        @Override
        public void onLoadNextPage(View view) {
            super.onLoadNextPage(view);
            RecyclerViewLoadingFooter.State state = RecyclerViewStateUtils.getFooterViewState(listWeChats);
            if (state == RecyclerViewLoadingFooter.State.Loading) {
                return;
            }

            if (hasMore) {
                RecyclerViewStateUtils.setFooterViewState(WChatListActivity.this, listWeChats, PAGE_SIXE, RecyclerViewLoadingFooter.State.Loading, null);
                qrrDataFromServer();

            } else {
                RecyclerViewStateUtils.setFooterViewState(WChatListActivity.this, listWeChats, PAGE_SIXE, RecyclerViewLoadingFooter.State.TheEnd, null);
            }
        }
    };

    private View.OnClickListener mFooterClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            RecyclerViewStateUtils.setFooterViewState(WChatListActivity.this, listWeChats, PAGE_SIXE, RecyclerViewLoadingFooter.State.Loading, null);
            qrrDataFromServer();
        }
    };

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            try {

                int pos = (int) v.getTag();

                Toast.makeText(WChatListActivity.this, "pos : " + pos, Toast.LENGTH_SHORT).show();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };
}
