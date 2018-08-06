package com.yinbao.www.gvcalendardemo.date;

import android.graphics.Color;
import android.graphics.Typeface;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.yinbao.www.gvcalendardemo.R;
import com.yinbao.www.gvcalendardemo.base.BaseActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * 创建时间:2018/8/6
 * 编写人:Administrator
 * 包名路径:com.yinbao.www.gvcalendardemo.date
 * 功能描述:
 * 1,演示使用gridview显示日历
 * 2,日历上允许标记
 */

public class GridViewDateActivity extends BaseActivity {


    private GridView mGridView;
    private ImageView mImgAdd,mImgLess;
    private TextView mShowTime;
    private String year,month,day;//当前年月日
    private Set<String> mSet;//存放标记的日期
    private CalendarAdapter mAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_gridview_data;
    }

    @Override
    protected void initListener() {
        MyListener listener=new MyListener();
        mImgAdd.setOnClickListener(listener);
        mImgLess.setOnClickListener(listener);

    }


    /**
     * 点击事件逻辑处理
     */
    private class MyListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {

                //上一个月
                case R.id.id_img_show_time_less:
                    mAdapter.lessMonth();
                    mAdapter.upDataMonth();
                    mAdapter.notifyDataSetChanged();
                    addTextToTopTextView(mShowTime);

                    break;

                //下一个月
                case R.id.id_img_show_time_add:
                    mAdapter.addMonth();
                    mAdapter.upDataMonth();
                    mAdapter.notifyDataSetChanged();
                    addTextToTopTextView(mShowTime);
                    break;


            }
        }
    }

    /**
     * 添加头部的年份 闰哪月等信息
     *
     * @param view
     */
    public void addTextToTopTextView(TextView view) {
        StringBuffer textDate = new StringBuffer();
        textDate.append(mAdapter.getShowYear()).append("年").append(
            mAdapter.getShowMonth()).append("月").append("\t");
        view.setText(textDate);
        view.setTextColor(Color.WHITE);
        view.setTypeface(Typeface.DEFAULT_BOLD);
    }


    @Override
    protected void initData() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
       String currentDate = sdf.format(date);  //当期日期
        year= String.valueOf(currentDate.split("-")[0]);//获取当前的年
        month = String.valueOf(currentDate.split("-")[1]);//获取当前的月
        day = String.valueOf(currentDate.split("-")[2]);//获取当前的天
        mSet=new HashSet<>();//存放标记的日期,格式yyyyMMdd
        mSet.add("20180806");
        mSet.add("20180805");
        mSet.add("20180706");
        mSet.add("20180707");
        mSet.add("20180708");
        mSet.add("20180606");
        mSet.add("20180605");

        mAdapter=new CalendarAdapter(this,Integer.parseInt(year),Integer.parseInt(month),(year+month+day),mSet);
        mGridView.setAdapter(mAdapter);
        addTextToTopTextView(mShowTime);
    }

    @Override
    protected void initView() {
        mGridView= (GridView) findViewById(R.id.id_gv_show_date);
        mImgAdd= (ImageView) findViewById(R.id.id_img_show_time_add);
        mImgLess= (ImageView) findViewById(R.id.id_img_show_time_less);
        mShowTime= (TextView) findViewById(R.id.id_tv_show_time);

    }
}
