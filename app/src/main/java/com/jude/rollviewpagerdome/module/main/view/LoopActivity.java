package com.jude.rollviewpagerdome.module.main.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.jude.rollviewpager.RollPagerView;
import com.jude.rollviewpager.adapter.LoopPagerAdapter;
import com.jude.rollviewpagerdome.R;

/**
 * Created by zhuchenxi on 2016/12/13.
 */

public class LoopActivity extends AppCompatActivity {
    private RollPagerView mViewPager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple);
        mViewPager = (RollPagerView) findViewById(R.id.view_pager);
        mViewPager.setAdapter(new ImageLoopAdapter(mViewPager));
    }

    private class ImageLoopAdapter extends LoopPagerAdapter {
        int[] imgs = new int[]{

        };

        public ImageLoopAdapter(RollPagerView viewPager) {
            super(viewPager);
        }

        @Override
        public View getView(ViewGroup container, int position) {
            ImageView view = new ImageView(container.getContext());
            view.setScaleType(ImageView.ScaleType.CENTER_CROP);
            view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            view.setImageResource(imgs[position]);
            return view;
        }

        @Override
        public int getRealCount() {
            return imgs.length;
        }
    }
}
