package com.abellstarlite.bean.Interface;

/**
 * Created by ce-ztzheng on 2017/3/1.
 */

public interface IProbleEventBean {
    /**
     * @return 尿尿 = "P",
     * 换尿布 = "C",
     * 走失    =  "L",
     * 该换尿布 = "NC"
     */
    //String P="p";
    public String getKind();

    /**
     * @return yyyy-MM-dd HH:mm:ss
     */
    public String getEvent_time();
}