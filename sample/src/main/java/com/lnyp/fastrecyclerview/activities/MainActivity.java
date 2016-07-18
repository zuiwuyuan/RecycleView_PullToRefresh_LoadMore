package com.lnyp.fastrecyclerview.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.lnyp.fastrecyclerview.R;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnListView;

    private Button btnGridView;

    private Button btnAddHeaderFooter1;

    private Button btnAddHeaderFooter2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();

        initListener();
    }

    private void initView() {

        this.btnListView = (Button) findViewById(R.id.btnListView);
        this.btnGridView = (Button) findViewById(R.id.btnGridView);
        this.btnAddHeaderFooter1 = (Button) findViewById(R.id.btnAddHeaderFooter1);
        this.btnAddHeaderFooter2 = (Button) findViewById(R.id.btnAddHeaderFooter2);
    }

    private void initListener() {
        this.btnListView.setOnClickListener(this);
        this.btnGridView.setOnClickListener(this);
        this.btnAddHeaderFooter1.setOnClickListener(this);
        this.btnAddHeaderFooter2.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnListView:
                startActivity(new Intent(this, WChatListActivity.class));
                break;
            case R.id.btnGridView:
                startActivity(new Intent(this, WChatGridActivity.class));
                break;
            case R.id.btnAddHeaderFooter1:
                startActivity(new Intent(this, WChatListHeaderAndFooterActivity.class));
                break;
            case R.id.btnAddHeaderFooter2:
                startActivity(new Intent(this, WChatGridHeaderAndFooterActivity.class));
                break;
        }
    }
}
