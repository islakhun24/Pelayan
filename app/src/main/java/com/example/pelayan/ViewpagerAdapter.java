package com.example.pelayan;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class ViewpagerAdapter extends FragmentStatePagerAdapter {

    int tabCount;

    //Constructor to the class
    public ViewpagerAdapter(FragmentManager fm, int tabCount) {
        super(fm);
        //Initializing tab count
        this.tabCount= tabCount;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                MakananTempat tab1 = new MakananTempat();
                return tab1;
            case 1:
                MinumanTempat tab2 = new MinumanTempat();
                return tab2;
            case 2:
                PaketTempat tab3 = new PaketTempat();
                return tab3;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return tabCount;
    }
}
