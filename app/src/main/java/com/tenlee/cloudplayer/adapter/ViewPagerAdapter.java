package com.tenlee.cloudplayer.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by tenlee on 16-3-11.
 */
public class ViewPagerAdapter extends PagerAdapter {

    private List<View> data;

    public ViewPagerAdapter(List<View> data) {
        super();
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return (view == object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(data.get(position));
        return data.get(position);
    }
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(data.get(position));
    }
}
