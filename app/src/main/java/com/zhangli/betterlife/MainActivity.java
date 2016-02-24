package com.zhangli.betterlife;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.RadioButton;

import com.zhangli.betterlife.fragment.CheapFragment;
import com.zhangli.betterlife.fragment.MoreFragment;
import com.zhangli.betterlife.fragment.NearFragment;
import com.zhangli.betterlife.fragment.PocketFragment;
import com.zhangli.betterlife.fragment.SuperFragment;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends FragmentActivity implements View.OnClickListener{
    private RadioButton near_btn, cheap_btn, super_btn, pocket_btn, more_btn;
    private ViewPager mViewPager;
    private static final int TAB_NEAR = 0;  //附近
    private static final int TAB_CHEAP = 1; //找便宜
    private static final int TAB_FAVOR = 2; //特惠
    private static final int TAB_POCKET = 3;//口袋
    private static final int TAB_MORE = 4;  //更多


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_layout);

        inintView();

        ArrayList<Fragment> myFragmentList = new ArrayList<>();
        myFragmentList.add(NearFragment.newInstance());
        myFragmentList.add(CheapFragment.newInstance());
        myFragmentList.add(SuperFragment.newInstance());
        myFragmentList.add(PocketFragment.newInstance());
        myFragmentList.add(MoreFragment.newInstance());

        //添加数据到adapter的方法给viewpager
        PagerAdapter pagerAdapter = new PagerAdapter(getSupportFragmentManager(), myFragmentList);
        mViewPager.setAdapter(pagerAdapter);
        //缓存4张fragment（好像是这样）
        mViewPager.setOffscreenPageLimit(4);
    }

    public void inintView() {
        near_btn = (RadioButton) findViewById(R.id.near_btn);
        near_btn.setOnClickListener(this);
        cheap_btn = (RadioButton) findViewById(R.id.cheap_btn);
        cheap_btn.setOnClickListener(this);
        super_btn = (RadioButton) findViewById(R.id.super_btn);
        super_btn.setOnClickListener(this);
        pocket_btn = (RadioButton) findViewById(R.id.pocket_btn);
        pocket_btn.setOnClickListener(this);
        more_btn = (RadioButton) findViewById(R.id.more_btn);
        more_btn.setOnClickListener(this);

        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPagerListener();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.near_btn:
                mViewPager.setCurrentItem(0);
                break;
            case R.id.cheap_btn:
                mViewPager.setCurrentItem(1);
                break;
            case R.id.super_btn:
                mViewPager.setCurrentItem(2);
                break;
            case R.id.pocket_btn:
                mViewPager.setCurrentItem(3);
                break;
            case R.id.more_btn:
                mViewPager.setCurrentItem(4);
                break;
        }
    }
    public void clearChecked(){
        cheap_btn.setChecked(false);
        super_btn.setChecked(false);
        near_btn.setChecked(false);
        pocket_btn.setChecked(false);
        more_btn.setChecked(false);
    }

    public void viewPagerListener() {
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }
            @Override
            public void onPageSelected(int position) {
                clearChecked();
                switch (position) {
                    case TAB_NEAR:
                        near_btn.setChecked(true);
                        break;
                    case TAB_CHEAP:
                        cheap_btn.setChecked(true);
                        break;
                    case TAB_FAVOR:
                        super_btn.setChecked(true);
                        break;
                    case TAB_POCKET:
                        pocket_btn.setChecked(true);
                        break;
                    case TAB_MORE:
                        more_btn.setChecked(true);
                        break;
                }
            }
            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }


    /**fragmentlist*/
    public static class PagerAdapter extends FragmentPagerAdapter {
        private List<Fragment> list = new ArrayList<>();

        public PagerAdapter(FragmentManager fm, List<Fragment> list) {
            super(fm);
            this.list = list;
        }
        @Override
        public Fragment getItem(int position) {
            return list.get(position);
        }
        @Override
        public int getCount() {
            return list.size();
        }
    }
}
