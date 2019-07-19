package com.example.minidouying;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;


public class VideoActivity extends AppCompatActivity implements View.OnClickListener{



    VideoPlayerIJK ijkPlayer = null;
    Button btnSetting;
    Button btnStop;
    Button btnPlay;
    SeekBar seekBar;
    TextView tvTime;
    TextView tvLoadMsg;
    VerticalSeekBar seek;
    ProgressBar pbLoading;
    RelativeLayout rlLoading;
    TextView tvPlayEnd;
    RelativeLayout rlPlayer;
    Button headring;
    int mVideoWidth = 0;
    int mVideoHeight = 0;
    static String ttt="";
    private boolean isPortrait = true;

    private Handler handler;
    public static final int MSG_REFRESH = 1001;

    private boolean menu_visible = true;
    RelativeLayout rl_bottom;
    RelativeLayout r2;
    boolean isPlayFinish = false;
    public static void launch(Activity activity, String url) {
        Intent intent = new Intent(activity, VideoActivity.class);
        //intent.putExtra("url", url);
        ttt=url;
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ijkplay);
        init();
        initIJKPlayer();
    }


    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus && Build.VERSION.SDK_INT >= 19) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }
    VerticalSeekBar volumBar = null;
    private void init() {
        btnPlay = findViewById(R.id.btn_play);
        seekBar = findViewById(R.id.seekBar);
        btnSetting = findViewById(R.id.btn_setting);
        btnStop = findViewById(R.id.btn_stop);

        rl_bottom = (RelativeLayout) findViewById(R.id.include_play_bottom);
        VideoPlayerIJK ijkPlayerView = findViewById(R.id.ijkPlayer);
        seek=findViewById(R.id.seekhear);
        tvTime = findViewById(R.id.tv_time);
        tvLoadMsg = findViewById(R.id.tv_load_msg);
        pbLoading = findViewById(R.id.pb_loading);
        rlLoading = findViewById(R.id.rl_loading);
        tvPlayEnd = findViewById(R.id.tv_play_end);
        rlPlayer = findViewById(R.id.rl_player);
        headring = findViewById(R.id.btn_hear);
        headring.setOnClickListener(this);
        btnStop.setOnClickListener(this);
        ijkPlayerView.setOnClickListener(this);
        btnSetting.setOnClickListener(this);
        btnPlay.setOnClickListener(this);

        /*volumBar =
        volumBar.setOnSeekBarChangeListener();*/
        seek.setProgress(50);
        seek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                ijkPlayer.mMediaPlayer.setVolume((float)(seek.getProgress())/100,(float)(seek.getProgress())/100);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });


        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                //进度改变
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                //开始拖动
                handler.removeCallbacksAndMessages(null);

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                //停止拖动
                ijkPlayer.seekTo(ijkPlayer.getDuration() * seekBar.getProgress() / 100);
                handler.sendEmptyMessageDelayed(MSG_REFRESH, 100);
            }
        });

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case MSG_REFRESH:
                        if (ijkPlayer.isPlaying()) {
                            refresh();
                            handler.sendEmptyMessageDelayed(MSG_REFRESH, 50);
                        }

                        break;
                }

            }
        };
    }

    public static String removeCharAt(String s, int pos) {

        return s.substring(0, pos) + s.substring(pos + 1);// 使用substring()方法截取0-pos之间的字符串+pos之后的字符串，相当于将要把要删除的字符串删除
    }
    private void refresh() {
        long current = ijkPlayer.getCurrentPosition() / 1000;
        long duration = ijkPlayer.getDuration() / 1000;
        long current_second = current % 60;
        long current_minute = current / 60;
        long total_second = duration % 60;
        long total_minute = duration / 60;
        String time = current_minute + ":" + current_second + "/" + total_minute + ":" + total_second;
        tvTime.setText(time);
        if (duration != 0) {
            seekBar.setProgress((int) (current * 100 / duration));
        }

    }

    private void initIJKPlayer() {
        //加载native库
        try {
            IjkMediaPlayer.loadLibrariesOnce(null);
            IjkMediaPlayer.native_profileBegin("libijkplayer.so");
        } catch (Exception e) {
            this.finish();
        }

        ijkPlayer = findViewById(R.id.ijkPlayer);
        ijkPlayer.setListener(new VideoPlayerListener());
        //ijkPlayer.setVideoResource(R.raw.yuminhong);
        //ijkPlayer.setVideoResource(R.raw.big_buck_bunny);

        ijkPlayer.setVideoPath(removeCharAt(ttt,4));
        /*ijkPlayer.setVideoResource(R.raw.big_buck_bunny);
        ijkPlayer.setVideoPath("https://media.w3.org/2010/05/sintel/trailer.mp4");
        ijkPlayer.setVideoPath("http://vjs.zencdn.net/v/oceans.mp4");*/

        ijkPlayer.setListener(new VideoPlayerListener() {
            @Override
            public void onBufferingUpdate(IMediaPlayer mp, int percent) {
            }

            @Override
            public void onCompletion(IMediaPlayer mp) {
                refresh();
                handler.sendEmptyMessageDelayed(MSG_REFRESH, 50);
                isPlayFinish = false;
                mVideoWidth = mp.getVideoWidth();
                mVideoHeight = mp.getVideoHeight();
                videoScreenInit();
                //toggle();
                mp.start();
                rlLoading.setVisibility(View.GONE);
                // btnPlay.setText("播放");
                // btnStop.setText("播放");
            }

            @Override
            public boolean onError(IMediaPlayer mp, int what, int extra) {
                return false;
            }

            @Override
            public boolean onInfo(IMediaPlayer mp, int what, int extra) {
                return false;
            }

            @Override
            public void onPrepared(IMediaPlayer mp) {
                refresh();
                handler.sendEmptyMessageDelayed(MSG_REFRESH, 50);
                isPlayFinish = false;
                mVideoWidth = mp.getVideoWidth();
                mVideoHeight = mp.getVideoHeight();
                videoScreenInit();
                //toggle();
                mp.start();
                rlLoading.setVisibility(View.GONE);
            }

            @Override
            public void onSeekComplete(IMediaPlayer mp) {
            }

            @Override
            public void onVideoSizeChanged(IMediaPlayer mp, int width, int height, int sar_num, int sar_den) {
                mVideoWidth = mp.getVideoWidth();
                mVideoHeight = mp.getVideoHeight();
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        handler.sendEmptyMessageDelayed(MSG_REFRESH, 1000);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (ijkPlayer != null && ijkPlayer.isPlaying()) {
            ijkPlayer.stop();
        }
        IjkMediaPlayer.native_profileEnd();
        handler.removeCallbacksAndMessages(null);
    }

    @Override
    protected void onDestroy() {
        if (ijkPlayer != null) {
            ijkPlayer.stop();
            ijkPlayer.release();
            ijkPlayer = null;
        }

        super.onDestroy();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ijkPlayer:
                if (menu_visible == false) {
                    rl_bottom.setVisibility(View.VISIBLE);
                    Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.show_bottom);
                    rl_bottom.startAnimation(animation);
                    menu_visible = true;
                } else {
                    rl_bottom.setVisibility(View.INVISIBLE);
                    Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.move_bottom);
                    rl_bottom.startAnimation(animation);
                    menu_visible = false;
                }

                break;
            case R.id.btn_hear:
                if(seek.getVisibility()==View.INVISIBLE)
                {
                    seek.setVisibility(View.VISIBLE);
                }
                else
                {
                    seek.setVisibility(View.INVISIBLE);
                }
                break;
            case R.id.btn_setting:
                toggle();
                break;
            case R.id.btn_play:
                if (btnPlay.getText().toString().equals(getResources().getString(R.string.pause))) {
                    ijkPlayer.pause();
                    btnPlay.setText(getResources().getString(R.string.media_play));
                } else {
                    ijkPlayer.start();
                    btnPlay.setText(getResources().getString(R.string.pause));
                }
                break;
            case R.id.btn_stop:
                if (btnStop.getText().toString().equals(getResources().getString(R.string.stop))) {
                    ijkPlayer.stop();
                    /*ijkPlayer.mMediaPlayer.prepareAsync();
                    ijkPlayer.mMediaPlayer.seekTo(0);*/
                    btnStop.setText(getResources().getString(R.string.media_play));
                } else {
       //1111                ijkPlayer.setVideoResource(R.raw.big_buck_bunny);
                    ijkPlayer.setVideoPath(ttt);
                    btnStop.setText(getResources().getString(R.string.stop));
                }
                break;
        }
    }

    private void videoScreenInit() {
        if (isPortrait) {
            portrait();
        } else {
            lanscape();
        }
    }

    private void toggle() {
        if (!isPortrait) {
            portrait();
        } else {
            lanscape();
        }
    }

    private void portrait() {
        ijkPlayer.pause();
        isPortrait = true;
        headring.setVisibility(View.VISIBLE);

        //  seek.setVisibility(View.VISIBLE);
      /*  RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) seekBar.getLayoutParams();
        params.addRule(RelativeLayout.LEFT_OF,R.id.btn_stop);
        seekBar.setLayoutParams(params);*/
        seek.setVisibility(View.INVISIBLE);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        WindowManager wm = (WindowManager) this
                .getSystemService(Context.WINDOW_SERVICE);
        float width = wm.getDefaultDisplay().getWidth();
        float height = wm.getDefaultDisplay().getHeight();
        float ratio = width / height;
        if (width < height) {
            ratio = height/width;
        }

        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) rlPlayer.getLayoutParams();
        layoutParams.height = (int) (mVideoHeight * ratio);
        layoutParams.width = (int) width;
        rlPlayer.setLayoutParams(layoutParams);
        btnSetting.setVisibility(View.INVISIBLE);
        //btnSetting.setText(getResources().getString(R.string.fullScreek));
        ijkPlayer.start();
    }

    private void lanscape() {
        ijkPlayer.pause();
        isPortrait = false;
        headring.setVisibility(View.VISIBLE);
        seek.setVisibility(View.INVISIBLE);
        //  seek.setVisibility(View.VISIBLE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

       /* RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) seekBar.getLayoutParams();
        params.addRule(RelativeLayout.LEFT_OF,R.id.btn_hear);
        seekBar.setLayoutParams(params);*/

        WindowManager wm = (WindowManager) this
                .getSystemService(Context.WINDOW_SERVICE);
        float width = wm.getDefaultDisplay().getWidth();
        float height = wm.getDefaultDisplay().getHeight();
        float ratio = width / height;

        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) rlPlayer.getLayoutParams();

        layoutParams.height = (int) RelativeLayout.LayoutParams.MATCH_PARENT;
        layoutParams.width = (int) RelativeLayout.LayoutParams.MATCH_PARENT;
        rlPlayer.setLayoutParams(layoutParams);
        btnSetting.setText(getResources().getString(R.string.smallScreen));
        ijkPlayer.start();
    }

    /*  @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        String url = getIntent().getStringExtra("url");
        VideoView videoView = findViewById(R.id.video_container);
        final ProgressBar progressBar = findViewById(R.id.progress_bar);
        videoView.setMediaController(new MediaController(this));
        videoView.setVideoURI(Uri.parse(url));
        videoView.requestFocus();
        videoView.start();
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                progressBar.setVisibility(View.GONE);
            }
        });
        progressBar.setVisibility(View.VISIBLE);
    }*/
}

