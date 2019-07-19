package com.example.minidouying;


import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.example.minidouying.utils.Utils;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
//import com.example.minidou3.utils.Utils;

public class MainActivity extends AppCompatActivity {

    ViewPager pager = null;
    LayoutInflater layoutInflater = null;
    List<View> pages = new ArrayList<View>();
    private RecyclerView mRecyclerView;
    private R_Adapter mDemoAdapter;
    private RecyclerView mRv;
    private List<Video> mVideos = new ArrayList<>();
    private Retrofit retrofit;
    private IMiniDouyinService miniDouyinService = getMiniDouyinService();//=retrofit.create(IMiniDouyinService.class);
    private Timer timer = new Timer();
    private String[] mPermissionsArrays = new String[]{Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.INTERNET,
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.RECORD_AUDIO};
    private final static int REQUEST_PERMISSION = 123;
    private static final int REQUEST_EXTERNAL_STORAGE = 101;
    private static final int REQUEST_INTERNET = 11;
    private String[] permissions1 = new String[] {
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    private String[] permissions2 = new String[] {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.INTERNET
    };
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_hot:
                    break;
                case R.id.navigation_post:
                    if (Utils.isPermissionsReady(MainActivity.this,permissions2))
                    {
                        startActivity(new Intent(MainActivity.this, PostActivity.class));
                    }
                    else
                    {
                        Utils.reuqestPermissions(MainActivity.this,permissions2,REQUEST_INTERNET);
                    }
                    break;
                case R.id.navigation_user:
                    startActivity(new Intent(MainActivity.this, UserActivity.class));
                    break;
            }
            return false;
        }
    };
    ImageView im;
    Timer timer_1=new Timer();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        im=findViewById(R.id.imageaa);
        Mytask_2 task=new Mytask_2(im,timer_1);
        timer_1.schedule(task,0,5000);


        if (!checkPermissionAllGranted(mPermissionsArrays)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(mPermissionsArrays, REQUEST_PERMISSION);
            }
        } else {
            Toast.makeText(MainActivity.this, "已经获取所有所需权限", Toast.LENGTH_SHORT).show();
        }

        initRecycleView();

        initads();
        BottomNavigationView navView = findViewById(R.id.nav_view_main);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        findViewById(R.id.fab_main).setOnClickListener(v -> {
            if (Utils.isPermissionsReady(MainActivity.this,permissions1))
            {
                startActivity(new Intent(MainActivity.this, myCameraActivity.class));
            }
            else
            {
                Utils.reuqestPermissions(MainActivity.this,permissions1,REQUEST_EXTERNAL_STORAGE);
            }
        });
    }
    private IMiniDouyinService getMiniDouyinService() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(IMiniDouyinService.HOST)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        if (miniDouyinService == null) {
            miniDouyinService = retrofit.create(IMiniDouyinService.class);
        }
        return miniDouyinService;
    }

    private void addImage(int resId) {
        ImageView imageView = (ImageView) layoutInflater.inflate(R.layout.inmain_ads_aditem_activity, null);
        Glide.with(this)
                .load(resId)
                .error(R.drawable.error)
                .into(imageView);
        pages.add(imageView);
    }

    private void addImage(String path) {
        ImageView imageView = (ImageView) layoutInflater.inflate(R.layout.inmain_ads_aditem_activity, null);
        Glide.with(this)
                .load(path)
                .error(R.drawable.error)
                .into(imageView);
        pages.add(imageView);
    }

    private void initads()
    {
        layoutInflater = getLayoutInflater();
        pager = (ViewPager) findViewById(R.id.ads_view_pager);
        addImage("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1563477243468&di=cea0e6773d4ae86fb7dba59d0d5c877e&imgtype=0&src=http%3A%2F%2F5b0988e595225.cdn.sohucs.com%2Fq_70%2Cc_zoom%2Cw_640%2Fimages%2F20180806%2F65643011e79645b0b0c5c010cb4b7b3f.jpeg ");
        addImage("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1563477434346&di=f0a85286863e8e55d608459e14589701&imgtype=0&src=http%3A%2F%2Fgss0.baidu.com%2F-4o3dSag_xI4khGko9WTAnF6hhy%2Fzhidao%2Fpic%2Fitem%2Fc83d70cf3bc79f3dccde5a0ab8a1cd11728b29c8.jpg");
        addImage("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1563477453007&di=184ce2d9d9532fea452081fe37b244f7&imgtype=0&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201302%2F18%2F20130218150608_HAScd.jpeg");
        addImage("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1563477243468&di=cea0e6773d4ae86fb7dba59d0d5c877e&imgtype=0&src=http%3A%2F%2F5b0988e595225.cdn.sohucs.com%2Fq_70%2Cc_zoom%2Cw_640%2Fimages%2F20180806%2F65643011e79645b0b0c5c010cb4b7b3f.jpeg");
        addImage("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1563477272251&di=98e8876630a4b0a9e257b64fa8117ed2&imgtype=0&src=http%3A%2F%2Fwww.whnews.cn%2Fdszx%2Fxxfl%2Fhk91%2Fcustomer%2F24107%2FHlH6e21TD5xtEWnnLhQ94cGfXTKSUKY0PdXE6uV1.png");
        addImage("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1563477390381&di=a42cc9420463dd9aa96f70c307a56473&imgtype=0&src=http%3A%2F%2Fmmbiz.qpic.cn%2Fmmbiz_jpg%2FXrW1yAX7eIhBIkdxhUWh0FAutj0neuHqNOEOsbxZ3c2Q61j7YAwQOmBv4Up63riaJpvRK5DicqEfqUJlWskxfoJA%2F640%3Fwx_fmt%3Djpeg");
        addImage("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1564072209&di=cce2b54264f4a5b160d7f13249f5cec0&imgtype=jpg&er=1&src=http%3A%2F%2F5b0988e595225.cdn.sohucs.com%2Fq_70%2Cc_zoom%2Cw_640%2Fimages%2F20180522%2F7232cc04a74645769827387de07b89f9.gif");
        addImage("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1564072209&di=cce2b54264f4a5b160d7f13249f5cec0&imgtype=jpg&er=1&src=http%3A%2F%2F5b0988e595225.cdn.sohucs.com%2Fq_70%2Cc_zoom%2Cw_640%2Fimages%2F20180522%2F7232cc04a74645769827387de07b89f9.gif   ");
        ViewAdapter adapter = new ViewAdapter();
        adapter.setDatas(pages);
        pager.setAdapter(adapter);
        pager.setCurrentItem(1);
        Mytask mytask=new Mytask(pager);
        timer.schedule(mytask,0,3000);
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) pager.setCurrentItem(pages.size() - 2);
                else if (position == pages.size() - 1) pager.setCurrentItem(1);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView img,img_2;
        public TextView tv1,tv2,tv3,tv4;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.res_image_1);
            img_2 = itemView.findViewById(R.id.res_image_2);
            tv1=itemView.findViewById(R.id.tv_data);
            tv2=itemView.findViewById(R.id.tv_data_2);
            tv3=itemView.findViewById(R.id.tv_data_3);
            tv4=itemView.findViewById(R.id.tv_data_4);
        }

        public void bind(final Activity activity, final Video video, final Video video_2) {
            Glide.with(img.getContext()).load(video.getImageUrl()).into(img);
            Glide.with(img_2.getContext()).load(video_2.getImageUrl()).into(img_2);
            tv1.setText(video.getUserName());
            tv3.setText(video.getStudentId());
            tv2.setText(video_2.getUserName());
            tv4.setText(video_2.getStudentId());
            img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(video!=null)
                        VideoActivity.launch(activity, video.getVideoUrl());
                }
            });
            img_2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(video_2!=null)
                        VideoActivity.launch(activity, video_2.getVideoUrl());
                }
            });
        }
    }



    private void initRecycleView()
    {
        mRv = findViewById(R.id.rv_list_2);
        mRv.setLayoutManager(new LinearLayoutManager(this));
        mRv.setAdapter(new RecyclerView.Adapter<inmain_ads.MyViewHolder>() {
            @NonNull
            @Override
            public inmain_ads.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                return new inmain_ads.MyViewHolder(
                        LayoutInflater.from(MainActivity.this)
                                .inflate(R.layout.inmain_resourses_item_activity, viewGroup, false));
            }

            @Override
            public void onBindViewHolder(@NonNull inmain_ads.MyViewHolder viewHolder, int i) {
                final Video video = mVideos.get(2*i);
                final Video video_2 = mVideos.get(2*i+1);
                viewHolder.bind(MainActivity.this, video,video_2);
            }

            @Override
            public int getItemCount() {
                return mVideos.size();
            }
        });

        miniDouyinService.getvideos().enqueue(new Callback<GetVideoResponse>() {
            @Override
            public void onResponse(Call<GetVideoResponse> call, Response<GetVideoResponse> response) {
                if(response.body()!=null&&response.body().getVideo()!=null)
                {
                    mVideos = response.body().getVideo();
                    mRv.getAdapter().notifyDataSetChanged();
                    Toast.makeText(MainActivity.this, "success!", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(MainActivity.this,"failed",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<GetVideoResponse> call, Throwable throwable) {
                Toast.makeText(MainActivity.this,"failed:"+throwable.getMessage(),Toast.LENGTH_LONG).show();

            }
        });

        /*  mRecyclerView = findViewById(R.id.rv_list_2);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mDemoAdapter = new R_Adapter();
        mRecyclerView.setAdapter(mDemoAdapter);
        List<R_Data> list = new ArrayList<>();
        for(int i = 0; i < 100; ++ i) {
            R_Data data = new R_Data(i);
            list.add(data);
        }
        mDemoAdapter.setData(list);
        mDemoAdapter.notifyDataSetChanged();*/
    }

    private void survicerequest() {
        retrofit=new Retrofit.Builder()
                .baseUrl("http://test.androidcamp.bytedance.com/mini_douyin/invoke/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        miniDouyinService = getMiniDouyinService();//=retrofit.create(IMiniDouyinService.class);
    }

    private boolean checkPermissionAllGranted(String[] permissions) {
        // 6.0以下不需要
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        for (String permission : permissions) {
            if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                // 只要有一个权限没有被授予, 则直接返回 false
                return false;
            }
        }
        return true;
    }

}
