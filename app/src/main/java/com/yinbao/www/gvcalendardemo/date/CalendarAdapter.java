package com.yinbao.www.gvcalendardemo.date;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.util.ArraySet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.yinbao.www.gvcalendardemo.R;

import java.util.Set;


/**
 * 日历gridview中的每一个item显示的textview
 */
public class CalendarAdapter extends BaseAdapter {

    private static String TAG = "CalendarAdapter";
    private boolean isLeapyear = false;  //是否为闰年
    private int daysOfMonth = 0;      //某月的天数
    private int dayOfWeek = 0;        //具体某一天是星期几
    private int lastDaysOfMonth = 0;  //上一个月的总天数
    private Context context;
    private String[] dayNumber = new String[42];  //一个gridview中的日期存入此数组中
    private SpecialCalendar sc = null;
    private int currentYear = 0;
    private int currentMonth = 0;
    /**
     * 当前选中的日期位置
     */
    private int currentFlag = -1;
    /**
     * 当前选中天的字符串 例:20170830
     */
    private String currentDayStr;
    private Set<Integer> schDateTagFlag = new ArraySet<>();
    private String showYear = "";   //用于在头部显示的年份
    private String showMonth = "";  //用于在头部显示的月份
    private String animalsYear = "";
    private String leapMonth = "";   //闰哪一个月
    private Set<String> mSet = null;
    /**
     * 距离当前月的差(上一个月-1,当前月0,下一个月+1)
     */
    private int jumpMonth = 0;


    /**
     * @param context
     * @param year          当前的年
     * @param month         当前的月
     * @param currentDayStr 当前选中的日期格式(yyyymmdd)
     */
    public CalendarAdapter(Context context, int year, int month, String currentDayStr, Set<String> mSet) {
        this.context = context;
        sc = new SpecialCalendar();
        currentYear = year;
        currentMonth = month;  //得到跳转到的月份
        this.currentDayStr = currentDayStr;
        this.mSet = mSet;
        getCalendar(currentYear, currentMonth);
    }

    @Override
    public int getCount() {
        return dayNumber.length;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder myViewHolder = null;
        if (convertView == null || convertView.getTag() == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_select_time, null);
            myViewHolder = new ViewHolder(convertView);
            convertView.setTag(myViewHolder);
        } else {
            myViewHolder = (ViewHolder) convertView.getTag();
        }
        myViewHolder.mIdTvItemSelectTimeDay.setText(dayNumber[position]);
        myViewHolder.mIdTvItemSelectTimeDay.setTextColor(Color.GRAY);//不是当前月为灰,先全部默认为灰
        if (position < daysOfMonth + dayOfWeek && position >= dayOfWeek) {
            // 当前月信息显示
            myViewHolder.mIdTvItemSelectTimeDay.setTextColor(Color.BLACK);// 当月字体设黑
        }

        if (currentFlag != -1 && currentFlag == position) {
            //设置当天的背景
            myViewHolder.mIdTvItemSelectTimeDay.setBackgroundResource(R.color.colorPrimary);
            myViewHolder.mIdTvItemSelectTimeDay.setTextColor(Color.WHITE);
        } else {
            myViewHolder.mIdTvItemSelectTimeDay.setBackgroundColor(0);
        }

        //显示小圆点
        if (schDateTagFlag != null && schDateTagFlag.size() > 0) {
            if (schDateTagFlag.contains(position)) {
                if (myViewHolder.mIdImgItemSelectTimeLogo.getVisibility() != View.VISIBLE) {
                    myViewHolder.mIdImgItemSelectTimeLogo.setVisibility(View.VISIBLE);
                }
            } else {
                if (myViewHolder.mIdImgItemSelectTimeLogo.getVisibility() != View.GONE) {
                    myViewHolder.mIdImgItemSelectTimeLogo.setVisibility(View.GONE);
                }
            }
        } else {
            if (myViewHolder.mIdImgItemSelectTimeLogo.getVisibility() != View.GONE) {
                myViewHolder.mIdImgItemSelectTimeLogo.setVisibility(View.GONE);
            }
        }


        return convertView;
    }


    /**
     * 下一个月
     */
    public void addMonth() {
        jumpMonth++;
    }

    /**
     * 上一个月
     */
    public void lessMonth() {
        jumpMonth--;
    }


    /**
     * 跳转到指定时间
     *
     * @param year
     * @param month
     */
    public void setDate(int year, int month) {
        currentYear = year;
        currentMonth = month;
        upDataMonth();
    }

    /**
     * 更新日历数据
     */
    public void upDataMonth() {
        int stepYear;
        int stepMonth = currentMonth + jumpMonth;
        if (stepMonth > 0) {
            //下一个月
            if (stepMonth % 12 == 0) {
                stepYear = currentYear + stepMonth / 12 - 1;
                stepMonth = 12;
            } else {
                stepYear = currentYear + stepMonth / 12;
                stepMonth = stepMonth % 12;
            }
        } else {
            //上一个月
            stepYear = currentYear - 1 + stepMonth / 12;
            stepMonth = stepMonth % 12 + 12;
        }
        getCalendar(stepYear, stepMonth);
    }

    /**
     * 得到某年的某月的天数且这月的第一天是星期几
     *
     * @param year
     * @param month
     */
    private void getCalendar(int year, int month) {
        isLeapyear = sc.isLeapYear(year);              //是否为闰年
        daysOfMonth = sc.getDaysOfMonth(isLeapyear, month);  //某月的总天数
        dayOfWeek = sc.getWeekdayOfMonth(year, month);      //某月第一天为星期几
        lastDaysOfMonth = sc.getDaysOfMonth(isLeapyear, month - 1);  //上一个月的总天数
        getWeek(year, month);
    }

    /**
     * 将一个月中的每一天的值添加入数组dayNuber中
     *
     * @param year
     * @param month
     */
    private void getWeek(int year, int month) {
        schDateTagFlag.clear();
        currentFlag = -1;
        int j = 1;
        //得到当前月的所有日程日期(这些日期需要标记)
        for (int i = 0; i < dayNumber.length; i++) {
            if (i < dayOfWeek) {  //前一个月
                int temp = lastDaysOfMonth - dayOfWeek + 1;
                dayNumber[i] = (temp + i) + "";
            } else if (i < daysOfMonth + dayOfWeek) {//本月
                int day = i - dayOfWeek + 1;   //得到的日期
                dayNumber[i] = i - dayOfWeek + 1 + "";
                //对于当前月才去标记当前日期
                String yearStr = String.valueOf(year);
                String monthStr = getStr(String.valueOf(month), 2);
                String dayStr = getStr(String.valueOf(day), 2);
                String timeAll = yearStr + monthStr + dayStr;
                if (timeAll.equals(currentDayStr)) {//判断选中的位置
                    currentFlag = i;
                }
                if (mSet != null && mSet.size() > 0) {
                    if (mSet.contains(timeAll))//当前时间是否包含在标签的时间中,包含就加入标签的列表中
                        schDateTagFlag.add(i);
                }
                setShowYear(yearStr);
                setShowMonth(String.valueOf(month));
            } else {   //下一个月
                dayNumber[i] = j + "";
                j++;
            }
        }
    }


    /**
     * 获取当前时间 样式:20170830
     *
     * @param position
     *
     * @return
     */
    public String getItemTime(int position) {
        String month = getStr(getShowMonth(), 2);
        String day = getStr(getDateByClickItem(position), 2);
        return getShowYear() + month + day;

    }


    /**
     * 点击每一个item时返回item中的日期
     *
     * @param position
     *
     * @return
     */
    public String getDateByClickItem(int position) {
        return dayNumber[position];
    }

    /**
     * 在点击gridView时，得到这个月中第一天的位置
     *
     * @return
     */
    public int getStartPositon() {
        return dayOfWeek + 7;
    }

    /**
     * 在点击gridView时，得到这个月中最后一天的位置
     *
     * @return
     */
    public int getEndPosition() {
        return (dayOfWeek + daysOfMonth + 7) - 1;
    }

    public String getShowYear() {
        return showYear;
    }

    public void setShowYear(String showYear) {
        this.showYear = showYear;
    }

    public String getShowMonth() {
        return showMonth;
    }

    public void setShowMonth(String showMonth) {
        this.showMonth = showMonth;
    }

    public String getAnimalsYear() {
        return animalsYear;
    }

    public void setAnimalsYear(String animalsYear) {
        this.animalsYear = animalsYear;
    }

    public String getLeapMonth() {
        return leapMonth;
    }

    public void setLeapMonth(String leapMonth) {
        this.leapMonth = leapMonth;
    }


    public Set<String> getSet() {
        return mSet;
    }


    static class ViewHolder {
        ImageView mIdImgItemSelectTimeLogo;
        TextView mIdTvItemSelectTimeDay;

        ViewHolder(View view) {
            mIdTvItemSelectTimeDay = (TextView) view.findViewById(R.id.id_tv_item_select_time_day);
            mIdImgItemSelectTimeLogo = (ImageView) view.findViewById(R.id.id_img_item_select_time_logo);
        }
    }


    /**
     * 保留N位整数,不足前面补0
     *
     * @param date String
     * @param bit  位数
     *
     * @return
     */
    public static String getStr(String date, int bit) {
        while (date.length() < bit)
            date = "0" + date;
        return date;
    }



}