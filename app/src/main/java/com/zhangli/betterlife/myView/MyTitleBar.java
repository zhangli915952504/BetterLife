package com.zhangli.betterlife.myView;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.zhangli.betterlife.R;

public class MyTitleBar extends RelativeLayout  {
    private int  left_img, right_img;

    public MyTitleBar(Context context) {
        super(context);
    }
    public MyTitleBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(attrs, 0);
    }

    public MyTitleBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(attrs, defStyleAttr);
    }

    public void initView(AttributeSet attrs, int defStyleAttr) {
        final TypedArray a=getContext().obtainStyledAttributes(attrs,R.styleable.MyTitleBar,defStyleAttr,0);
        left_img=a.getResourceId(R.styleable.MyTitleBar_title_left_img,0);
        right_img=a.getResourceId(R.styleable.MyTitleBar_title_right_img,0);
        a.recycle();

        View v = View.inflate(getContext(),R.layout.mytitlebar_layout, this);
        if (left_img != 0) {
            ImageView left = (ImageView) v.findViewById(R.id.title_left_img);
            left.setImageResource(left_img);

        }
        if (right_img != 0) {
            ImageView right = (ImageView) v.findViewById(R.id.title_right_img);
            right.setImageResource(right_img);
        }
    }
}
