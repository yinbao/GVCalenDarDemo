package com.yinbao.www.gvcalendardemo.base;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;



/**
 * activity的基类，所有的activity都要继承该类
 * 所有activity共有的功能都可以写在这里
 * Created by xing on 2016/8/30.
 */
public abstract class BaseActivity extends AppCompatActivity {
    /**
     * 上下文对象
     */
    protected Context mContext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getLayoutId() != 0) {
            initWindows();
            setContentView(getLayoutId());
            SaveActivityData(savedInstanceState);
            mContext = this;
            init();
        } else if (getLayoutView() != null) {
            initWindows();
            setContentView(getLayoutView());
            SaveActivityData(savedInstanceState);
            mContext = this;
            init();
        }
    }

    protected void SaveActivityData(Bundle savedInstanceState) {

    }


    /**
     * 在绑定布局前的操作（状态，任务栏等的设置）
     */
    protected void initWindows() {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//禁止横屏
    }

    /**
     * 获取当前activity的布局
     *
     * @return int
     */
    protected abstract int getLayoutId();

    /**
     * 获取当前activity的布局
     *
     * @return View
     */
    protected View getLayoutView() {
        return null;
    }

    /**
     * 初始化
     */
    protected final void init() {
        initView();
        initData();
        initListener();
    }




    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            myFinish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 返回
     */
    protected void myFinish() {
        finish();
    }


    /**
     * 初始化事件
     */
    protected abstract void initListener();

    /**
     * 初始化数据
     */
    protected abstract void initData();

    /**
     * 初始化，绑定布局
     */
    protected abstract void initView();


    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }


    @Override
    protected void onStop() {
        super.onStop();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
