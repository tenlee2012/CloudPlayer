package com.tenlee.cloudplayer.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.Build;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;

import com.astuetz.PagerSlidingTabStrip;
import com.astuetz.viewpager.extensions.sample.SuperAwesomeCardFragment;
import com.tenlee.cloudplayer.MusicApplication;
import com.tenlee.cloudplayer.fragment.MyMusicListFragment;
import com.tenlee.cloudplayer.R;
import com.tenlee.cloudplayer.fragment.NetMusicListFragment;
import com.tenlee.cloudplayer.service.PlayService;
import com.tenlee.cloudplayer.utils.Constant;

public class MainActivity extends BaseActivity {

    private final Handler handler = new Handler();

    private PagerSlidingTabStrip tabs;
    private ViewPager pager;
    private MyPagerAdapter adapter;
    private MyMusicListFragment myMusicListFragment;
    private NetMusicListFragment netMusicListFragment;

    private Drawable oldBackground = null;
    private int currentColor = 0xFF3F9FE0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        pager = (ViewPager) findViewById(R.id.pager);
        adapter = new MyPagerAdapter(getSupportFragmentManager());

        pager.setAdapter(adapter);

        final int pageMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, getResources()
                .getDisplayMetrics());
        pager.setPageMargin(pageMargin);

        tabs.setViewPager(pager);
        changeColor(currentColor);

        //bindPlayService(); //绑定播放服务
    }

    /**
     * 保存本次退出时播放的信息
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        SharedPreferences.Editor editor = MusicApplication.sp.edit();
        editor.putInt(Constant.CURRENTPOSITIONINMP3LIST, playService.getCurrentPositionInMp3list());
        editor.putInt(Constant.PLAY_MODE, playService.getPlay_mode());
        editor.commit();
    }

    private void changeColor(int newColor) {

        tabs.setIndicatorColor(newColor);

        // change ActionBar color just if an ActionBar is available
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {

            Drawable colorDrawable = new ColorDrawable(newColor);
            Drawable bottomDrawable = getResources().getDrawable(R.drawable.actionbar_bottom);
            LayerDrawable ld = new LayerDrawable(new Drawable[]{colorDrawable, bottomDrawable});

            if (oldBackground == null) {

                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    ld.setCallback(drawableCallback);
                } else {
                    getActionBar().setBackgroundDrawable(ld);
                }

            } else {

                TransitionDrawable td = new TransitionDrawable(new Drawable[]{oldBackground, ld});
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    td.setCallback(drawableCallback);
                } else {
                    getActionBar().setBackgroundDrawable(td);
                }

                td.startTransition(200);

            }

            oldBackground = ld;

            getActionBar().setDisplayShowTitleEnabled(false);
            getActionBar().setDisplayShowTitleEnabled(true);

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menu_ilike:
                startActivity(new Intent(this, LoveMusicActivity.class));
                break;
            case R.id.menu_near_play:
                break;
            case R.id.menu_exit:
                stopService(new Intent(this, PlayService.class));
                finish();
                break;
        }
        return true;
    }

    private Drawable.Callback drawableCallback = new Drawable.Callback() {
        @Override
        public void invalidateDrawable(Drawable who) {
            getActionBar().setBackgroundDrawable(who);
        }

        @Override
        public void scheduleDrawable(Drawable who, Runnable what, long when) {
            handler.postAtTime(what, when);
        }

        @Override
        public void unscheduleDrawable(Drawable who, Runnable what) {
            handler.removeCallbacks(what);
        }
    };

    @Override
    public void publish(int progress) {
        //更新进度条
    }

    @Override
    public void change(int position) {
        //切换状态播放位置
        if (pager.getCurrentItem() == 0) {
            myMusicListFragment.loadData(); //此时加载音乐
            myMusicListFragment.changeUIStatusOnPlay(position);
        } else if (pager.getCurrentItem() == 1) {

        }

    }

    public class MyPagerAdapter extends FragmentPagerAdapter {

        private final String[] TITLES = { "本地音乐", "在线音乐", "我" };

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return TITLES[position];
        }

        @Override
        public int getCount() {
            return TITLES.length;
        }

        @Override
        public Fragment getItem(int position) {
            Log.d("mydebug", position+"");
            if (position == 0) { //本地音乐
                if(myMusicListFragment == null) {
                    myMusicListFragment =  MyMusicListFragment.newInstance();
                }
                return myMusicListFragment;
            } else if (position == 1 ) { //网络音乐
                if(netMusicListFragment == null) {
                    netMusicListFragment =  NetMusicListFragment.newInstance();
                }
                return netMusicListFragment;
            }
//            return MyMusicListFragment.newInstance(position);
            return SuperAwesomeCardFragment.newInstance(position);
        }

    }
}
