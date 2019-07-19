package com.example.minidouying;

import android.view.View;
import android.widget.ImageView;

import java.util.Timer;
import java.util.TimerTask;

public class Mytask_2 extends TimerTask {
    private ImageView imm;
    private Timer timerr;

    public Mytask_2(ImageView im, Timer timer) {
        imm=im;
        timerr=timer;
    }

    @Override
    public void run() {
        imm.post(new Runnable() {
            @Override
            public void run() {
                imm.setVisibility(View.INVISIBLE);
                timerr.cancel();
            }
        });
    }
}

