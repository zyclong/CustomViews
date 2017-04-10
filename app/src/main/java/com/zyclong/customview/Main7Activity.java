package com.zyclong.customview;

import android.animation.LayoutTransition;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mikepenz.itemanimators.SlideDownAlphaAnimator;

import java.util.ArrayList;
import java.util.List;


public class Main7Activity extends AppCompatActivity {
    static String TAG="Main7Activity";

    private RecyclerView mRecyclerView;
    private List<String> mDatas;
    private HomeAdapter mAdapter;
    Animation as;
    Animation asGone;
    String str;
    Handler mHandler;
    LinearLayout l;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main7);

            initData();

            mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            mRecyclerView.setItemAnimator(new SlideDownAlphaAnimator());
            mRecyclerView.addItemDecoration(new DividerItemDecoration(
                    this, DividerItemDecoration.VERTICAL_LIST));
            mRecyclerView.setAdapter(mAdapter = new HomeAdapter());
        }catch (Exception e){
            Log.e(TAG, "onCreate: ",e );
        }
    }

    private void initData() {
        str=getResources().getString(R.string.long_text);
        mDatas = new ArrayList<String>();
        for (int i = 'A'; i < 'z'; i++)
        {
            mDatas.add(str);
        }
        as= AnimationUtils.loadAnimation(this,R.anim.tv_item_help);

        asGone= AnimationUtils.loadAnimation(this,R.anim.tv_item_help_gone);
        mHandler=new Handler();
    }

     class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.MyViewHolder>
    {

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
        {
            MyViewHolder holder = new MyViewHolder(LayoutInflater.from(
                    Main7Activity.this).inflate(R.layout.item_help, parent,
                    false));
            return holder;
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position)
        {
            holder.tv.setText(mDatas.get(position));
        }

        @Override
        public int getItemCount()
        {
            return mDatas.size();
        }

          class MyViewHolder extends RecyclerView.ViewHolder {


            TextView tv;
            Button btn;
              int h;
              int count;
              private Runnable r;


              public MyViewHolder(View view)
            {
                super(view);
                try {
                    l = (LinearLayout)view.findViewById(R.id.l);
                    LayoutTransition transition = new LayoutTransition();
                    transition.setDuration(1000);
                    l.setLayoutTransition(transition);
                    tv = (TextView) view.findViewById(R.id.tv_item_help);
                    btn = (Button) view.findViewById(R.id.btn_item_help);
                    count = 10;
                    r = new Runnable() {
                        @Override
                        public void run() {
                            try {
                                LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) tv.getLayoutParams();
                                lp.height = h / count;
                                tv.setLayoutParams(lp);
                            } catch (Exception e) {
                                Log.e("eee", "run: ", e);
                            }

                        }
                    };


                    as.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {
                            h = tv.getHeight();
                            for (int i = 1; i < count; count--) {
                                //   mHandler.postDelayed(r,100);
                            }
                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {

                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });
                    btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (tv.getVisibility() == View.VISIBLE) {
                                tv.setAnimation(asGone);
                                tv.setVisibility(View.GONE);
                            } else {
                                tv.setVisibility(View.VISIBLE);

                                tv.startAnimation(as);
//                            for(int i=1;i<str.length();i++){
//                                tv.setText(str.substring(0,i));
//                            }
                            }
                        }
                    });
                }catch (Exception e){
                    Log.e(TAG, "MyViewHolder: ",e );
                }
            }
        }
    }

    private static class DividerItemDecoration extends RecyclerView.ItemDecoration {
        private static final int[] ATTRS = new int[]{
                android.R.attr.listDivider
        };

        public static final int HORIZONTAL_LIST = LinearLayoutManager.HORIZONTAL;

        public static final int VERTICAL_LIST = LinearLayoutManager.VERTICAL;

        private Drawable mDivider;

        private int mOrientation;

        public DividerItemDecoration(Context context, int orientation) {
            final TypedArray a = context.obtainStyledAttributes(ATTRS);
            mDivider = a.getDrawable(0);
            a.recycle();
            setOrientation(orientation);
        }

        public void setOrientation(int orientation) {
            if (orientation != HORIZONTAL_LIST && orientation != VERTICAL_LIST) {
                throw new IllegalArgumentException("invalid orientation");
            }
            mOrientation = orientation;
        }

        @Override
        public void onDraw(Canvas c, RecyclerView parent) {
            if (mOrientation == VERTICAL_LIST) {
                drawVertical(c, parent);
            } else {
                drawHorizontal(c, parent);
            }
        }

        public void drawVertical(Canvas c, RecyclerView parent) {
            final int left = parent.getPaddingLeft();
            final int right = parent.getWidth() - parent.getPaddingRight();

            final int childCount = parent.getChildCount();
            for (int i = 0; i < childCount; i++) {
                final View child = parent.getChildAt(i);
                final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                        .getLayoutParams();
                final int top = child.getBottom() + params.bottomMargin;
                final int bottom = top + mDivider.getIntrinsicHeight();
                mDivider.setBounds(left, top, right, bottom);
                mDivider.draw(c);
            }
        }

        public void drawHorizontal(Canvas c, RecyclerView parent) {
            final int top = parent.getPaddingTop();
            final int bottom = parent.getHeight() - parent.getPaddingBottom();

            final int childCount = parent.getChildCount();
            for (int i = 0; i < childCount; i++) {
                final View child = parent.getChildAt(i);
                final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                        .getLayoutParams();
                final int left = child.getRight() + params.rightMargin;
                final int right = left + mDivider.getIntrinsicHeight();
                mDivider.setBounds(left, top, right, bottom);
                mDivider.draw(c);
            }
        }

        @Override
        public void getItemOffsets(Rect outRect, int itemPosition, RecyclerView parent) {
            if (mOrientation == VERTICAL_LIST) {
                outRect.set(0, 0, 0, mDivider.getIntrinsicHeight());
            } else {
                outRect.set(0, 0, mDivider.getIntrinsicWidth(), 0);
            }
        }
    }
}
