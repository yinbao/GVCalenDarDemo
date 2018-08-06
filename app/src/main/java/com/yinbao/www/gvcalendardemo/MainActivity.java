package com.yinbao.www.gvcalendardemo;

import android.content.Intent;
import android.view.View;
import android.widget.Button;

import com.yinbao.www.gvcalendardemo.base.BaseActivity;
import com.yinbao.www.gvcalendardemo.date.GridViewDateActivity;

public class MainActivity extends BaseActivity {

    private Button mButton;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initListener() {
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, GridViewDateActivity.class));
            }
        });

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {
        mButton = (Button) findViewById(R.id.id_btn_date);
    }
}
