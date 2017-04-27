package com.abellstarlite.wedgit.zyclong.bean;


import com.anye.greendao.gen.AppWifiInfoDao;
import com.zyclong.app.GreenDaoUpdateUtils;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Property;

import java.util.Map;

/**
 * Created by zyclong on 2017/4/17.
 * WifiInfo保存在数据库中的javabean
 */
@Entity
public class AppWifiInfo implements GreenDaoUpdateUtils.Update{
    /**
     * 写入数据库时的时间
     */
    public static final String LASTTIMEMode="yyyyMMdd_HHmmss";
    /**
     * id 由greendao管理
     */
    @Id
    private Long id;
    /**
     * 写入数据库时的时间，格式
     * @see #LASTTIMEMode
     */
    @Property(nameInDb = "LASTTIME")
    private String LASTTIME;
    /**
     * wifi的ssid
     */
    @Property(nameInDb = "SSID")
    private String SSID;
    /**
     * wifi的密码
     */
    @Property(nameInDb = "PASSWORD")
    private String PASSWORD;
    /**
     * wifi的加密方式，保留，暂未使用
     */
    @Property (nameInDb = "KEYMGMT")
    private String KEYMGMT;

    /**
     * wifi的加密方式，保留，暂未使用
     */
    @Property (nameInDb = "UP")
    @NotNull
    private String up;



    @Generated(hash = 372010525)
    public AppWifiInfo(Long id, String LASTTIME, String SSID, String PASSWORD, String KEYMGMT, @NotNull String up) {
        this.id = id;
        this.LASTTIME = LASTTIME;
        this.SSID = SSID;
        this.PASSWORD = PASSWORD;
        this.KEYMGMT = KEYMGMT;
        this.up = up;
    }

    @Generated(hash = 609805122)
    public AppWifiInfo() {
    }


    public String getPASSWORD() {
        return this.PASSWORD;
    }

    public void setPASSWORD(String PASSWORD) {
        this.PASSWORD = PASSWORD;
    }

    public String getSSID() {
        return this.SSID;
    }

    public void setSSID(String SSID) {
        this.SSID = SSID;
    }

    public String getLASTTIME() {
        return this.LASTTIME;
    }


    public Long getId() {
        return this.id;
    }



    public void setId(Long id) {
        this.id = id;
    }

    public String getKEYMGMT() {
        return this.KEYMGMT;
    }

    public void setKEYMGMT(String KEYMGMT) {
        this.KEYMGMT = KEYMGMT;
    }

    public void setLASTTIME(String LASTTIME) {
        this.LASTTIME = LASTTIME;
    }


    @Override
    public String toString() {
        return getId()+"--"+getLASTTIME()+"--"+getKEYMGMT()+"--"+getPASSWORD()+"--"+getSSID()+"--"+getUp();
    }

    /**
     * 参数为数据库中每一行所有列的集合
     * 每一列的数据类型请参阅sqlite的数据类型
     *
     * @param oneRow 数据库中每一行的数据
     */
    @Override
    public void updateOne(Map<String, Object> oneRow ) {
        this.setUp("up");

        this.setId(null);
        this.setKEYMGMT((String) oneRow.get(AppWifiInfoDao.Properties.KEYMGMT.name));
        this.setSSID((String) oneRow.get(AppWifiInfoDao.Properties.SSID.name));
        this.setPASSWORD((String) oneRow.get(AppWifiInfoDao.Properties.PASSWORD.name));
        this.setLASTTIME((String) oneRow.get(AppWifiInfoDao.Properties.LASTTIME.name));
    }

    public String getUp() {
        return this.up;
    }

    public void setUp(String up) {
        this.up = up;
    }

}
