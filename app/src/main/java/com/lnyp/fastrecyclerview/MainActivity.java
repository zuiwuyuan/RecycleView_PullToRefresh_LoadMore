package com.lnyp.fastrecyclerview;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnListView;

    private Button btnGridView;

    private Button btnAddHeaderFooter;

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
        this.btnAddHeaderFooter = (Button) findViewById(R.id.btnAddHeaderFooter);
    }

    private void initListener() {
        this.btnListView.setOnClickListener(this);
        this.btnGridView.setOnClickListener(this);
        this.btnAddHeaderFooter.setOnClickListener(this);
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
            case R.id.btnAddHeaderFooter:

                break;
        }
    }
}
