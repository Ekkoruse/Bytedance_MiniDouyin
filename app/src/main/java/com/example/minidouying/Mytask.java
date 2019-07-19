package com.example.minidouying;

import androidx.viewpager.widget.ViewPager;

import java.util.TimerTask;

public class Mytask extends TimerTask {
    ViewPager p;
    public Mytask(ViewPager pager) {
        p=pager;
    }

    @Override
    public void run() {
        p.post(new Runnable() {
            @Override
            public void run() {
                p.setCurrentItem(p.getCurrentItem()+1);
            }
        });
    }
}
