package com.lnyp.fastrecyclerview.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.cundong.recyclerview.EndlessRecyclerOnScrollListener;
import com.cundong.recyclerview.HeaderAndFooterRecyclerViewAdapter;
import com.cundong.recyclerview.HeaderSpanSizeLookup;
import com.cundong.recyclerview.LoadingFooter;
import com.cundong.recyclerview.RecyclerViewStateUtils;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.util.LogUtils;
import com.lnyp.fastrecyclerview.R;
import com.lnyp.fastrecyclerview.adapter.WeChatListAdapter;
import com.lnyp.fastrecyclerview.bean.WeChatModel;
import com.lnyp.fastrecyclerview.http.HttpUtil;
import com.lnyp.fastrecyclerview.http.IOAuthCallBack;
import com.lnyp.fastrecyclerview.resp.RespWeChats;
import com.lnyp.fastrecyclerview.util.GsonUtils;
import com.yqritc.recyclerviewflexibledivider.GridSpacingItemDecoration;

import java.util.ArrayList;
import java.util.List;

public class WChatGridActivity extends AppCompatActivity {

    private static final String URL = "http://v.juhe.cn/weixin/query";

    private static final int PAGE_SIXE = 10;

    public RecyclerView listWeChats;

    private List<WeChatModel> mDatas;

    private HeaderAndFooterRecyclerViewAdapter recyclerViewAdapter;

    private int pno = 1;

    private boolean hasMore = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wchat_grid);

        initView();

        qrrDataFromServer();
    }

    private void initView() {

        listWeChats = (RecyclerView) findViewById(R.id.listWeChats);

        mDatas = new ArrayList<>();

        WeChatListAdapter weChatListAdapter = new WeChatListAdapter(this, mDatas, onClickListener);

        recyclerViewAdapter = new HeaderAndFooterRecyclerViewAdapter(weChatListAdapter);
        listWeChats.setAdapter(recyclerViewAdapter);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        gridLayoutManager.setSpanSizeLookup(new HeaderSpanSizeLookup((HeaderAndFooterRecyclerViewAdapter) listWeChats.getAdapter(), gridLayoutManager.getSpanCount()));
        listWeChats.setLayoutManager(gridLayoutManager);

        listWeChats.addItemDecoration(new GridSpacingItemDecoration(this, 2, 100, 20, false, false));

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
                        RecyclerViewStateUtils.setFooterViewState(listWeChats, LoadingFooter.State.Normal);
                        break;
                    case 400:
                        RecyclerViewStateUtils.setFooterViewState(WChatGridActivity.this, listWeChats, PAGE_SIXE, LoadingFooter.State.NetWorkError, mFooterClick);
                        break;
                }

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
            LoadingFooter.State state = RecyclerViewStateUtils.getFooterViewState(listWeChats);

            if (state == LoadingFooter.State.Loading) {
                LogUtils.e("the state is Loading, just wait..");
                return;
            }

            if (hasMore) {
                RecyclerViewStateUtils.setFooterViewState(WChatGridActivity.this, listWeChats, PAGE_SIXE, LoadingFooter.State.Loading, null);
                qrrDataFromServer();

            } else {
                RecyclerViewStateUtils.setFooterViewState(WChatGridActivity.this, listWeChats, PAGE_SIXE, LoadingFooter.State.TheEnd, null);
            }
        }
    };

    private View.OnClickListener mFooterClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            RecyclerViewStateUtils.setFooterViewState(WChatGridActivity.this, listWeChats, PAGE_SIXE, LoadingFooter.State.Loading, null);
            qrrDataFromServer();
        }
    };

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            try {
                int pos = (int) v.getTag();

                Toast.makeText(WChatGridActivity.this, "pos : " + pos, Toast.LENGTH_SHORT).show();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };
}
