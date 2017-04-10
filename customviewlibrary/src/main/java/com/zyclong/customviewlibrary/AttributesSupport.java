package com.zyclong.customviewlibrary;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by zyclong-pc on 2017/2/27.
 */

public class AttributesSupport extends ViewGroup {
    public AttributesSupport(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            int childCount=getChildCount();
        for(int i=0;i<childCount;i++){
            View v=getChildAt(i);
           // v.get
           // v.setPadding();
          //  LayoutParams lp=v.getLayoutParams();
           // lp.
           // PercentLayoutHelper

        }

    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

    }

    public interface AttributeSuppout{

       // setAttrbitue(View v ,);

    }
}
