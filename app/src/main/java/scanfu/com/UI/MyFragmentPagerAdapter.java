package scanfu.com.UI;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class MyFragmentPagerAdapter extends FragmentPagerAdapter {
    private final int PAGER_COUNT = 4;
    FuKaFragment mFuKaFragment = null;
    SaoMiaoFragment mSaoMiaoFragment=null;
    FrendFragment mFrendFragment=null;
    MeFragment mMeFragment=null;

    public MyFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
        mFuKaFragment = new FuKaFragment();
        mSaoMiaoFragment = new SaoMiaoFragment();
        mFrendFragment = new FrendFragment();
        mMeFragment = new MeFragment();
    }

    @Override
    public Fragment getItem(int i) {
        switch (i){
            case 0:return mFuKaFragment;
            case 1:return mSaoMiaoFragment;
            case 2:return mFrendFragment;
            case 3:return mMeFragment;
        }
        return null;
    }

    @Override
    public int getCount() {
        return PAGER_COUNT;
    }
}
