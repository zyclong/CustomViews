package com.zyclong.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by zyclong-pc on 2017/3/8.
 */

public class StringUtils {
    private static final String pattern="^\\d+[.]*\\d*%";
    public static float getFloatFromPercentcentString(String str){
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(str);
        if(!m.matches()){
            throw new IllegalArgumentException(str+" not a percent String");
        }
        float s=Float.parseFloat(str.substring(0,str.length()-1));
        return s/100;
    }
}

