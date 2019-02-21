package ictandroid.youtube.com.Campaign;

;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import ictandroid.youtube.com.Campaign.AllChannel.FragmentAllChannel;
import ictandroid.youtube.com.Campaign.MyChannel.FragmentMyChannel;

public class PagerAdapter extends FragmentPagerAdapter {
    private int numOfTabs;
    public PagerAdapter(FragmentManager fm, int numOfTabs) {
        super(fm);
        this.numOfTabs = numOfTabs;
    }

    @Override
    public Fragment getItem(int i) {
        switch (i) {
            case 0:
                return new FragmentAllChannel();
            case 1:
                return new FragmentMyChannel();
            default:
                return null;
        }

    }

    @Override
    public int getCount() {
        return numOfTabs;
    }

}