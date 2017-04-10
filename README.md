# CustomViews
自定义View</br>
BarChart 柱状图</br>
DataDayTextView 和下面的配合使用</br>
DataDayView 用线条显示数据</br>
LineChart 折线图</br>
ProgerssBarARC 弧形进度条</br>
RadarScan 高仿微信的雷达扫描器</br>
RadarScan_2（RadarScan的历史版本）</br>
RoundedImageView 圆形的ImageView，不是我写的</br>
SeekBarWithNumber 可以加上数字显示的seekbar</br>
AppUtils 可以得到项目是否为debug阶段，替代BuildConfig.DEBUG,先使用syncIsDebug() 在获取isDebug()</br>

<pre>
数据接口
package com.abellstarlite.bean.Interface;

/**
 * 
 */

public interface IProbleEventBean {
    /**
     * @return "P"或者"C"
     */

    public String getKind();

    /**
     * @return yyyy-MM-dd HH:mm:ss
     */
    public String getEvent_time();
}
</pre>
