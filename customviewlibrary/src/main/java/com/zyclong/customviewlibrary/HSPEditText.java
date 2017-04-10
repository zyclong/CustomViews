package com.zyclong.customviewlibrary;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.EditText;

/**
 * Created by zyclong-pc on 2017/2/27.
 */

public class HSPEditText extends EditText {
    public HSPEditText(Context context) {
        super(context);
    }

    public HSPEditText(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public HSPEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
        
    }

    private void init(AttributeSet attrs) {
    }

}
