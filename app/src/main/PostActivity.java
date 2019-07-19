package com.example.minidou3;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.annotation.NonNull;
//import androidx.support.annotation.NonNull;
//import android.support.v7.app.AppCompatActivity;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.minidou3.api.GetVideoResponse;
import com.example.minidou3.api.IMiniDouyinService;
import com.example.minidou3.api.PostVideoResponse;
import com.example.minidou3.model.Video;
import com.example.minidou3.ImageHelper;
import com.example.minidou3.utils.ResourceUtils;
/*import com.bytedance.androidcamp.network.dou.api.GetVideoResponse;
import com.bytedance.androidcamp.network.dou.api.IMiniDouyinService;
import com.bytedance.androidcamp.network.dou.api.PostVideoResponse;
import com.bytedance.androidcamp.network.dou.model.Video;
import com.bytedance.androidcamp.network.lib.util.ImageHelper;
import com.bytedance.androidcamp.network.dou.util.ResourceUtils;
*/
import com.example.minidou3.model.Video;
import com.example.minidou3.utils.Utils;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PostActivity extends AppCompatActivity {

    private static final int PICK_IMAGE = 1;
    private static final int PICK_VIDEO = 2;
    private static final String TAG = "PostActivity";

    public Uri mSelectedImage;
    private Uri mSelectedVideo;
    public Button btn_v,btn_i,btn_p;
    public ImageView mImageView;
    public VideoView mVideoView;

    private Runnable runnable;
    private Handler handler;


    private Retrofit retrofit=new Retrofit.Builder()
            .baseUrl("http://test.androidcamp.bytedance.com/mini_douyin/invoke/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    private IMiniDouyinService miniDouyinService = getMiniDouyinService();//=retrofit.create(IMiniDouyinService.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        btn_v = findViewById(R.id.btn_choose_video);
        btn_i = findViewById(R.id.btn_choose_img);
        btn_p = findViewById(R.id.btn_post);
        btn_p.setEnabled(false);
        mImageView = findViewById(R.id.imageView);
        mVideoView = findViewById(R.id.videoView);
        initBtns();
        handler = new Handler();
        runnable = new Runnable() {

            @Override
            public void run() {

                if(null!=mSelectedImage){
                    mImageView.setImageURI(mSelectedImage);
                }
                if(null!=mSelectedVideo){
                    mVideoView.setVideoURI(mSelectedVideo);
                    if(!mVideoView.isPlaying()){
                        mVideoView.start();
                    }
                }
                if (mSelectedVideo != null && mSelectedImage != null) {
                    btn_p.setText("现在就上传！");
                    btn_p.setEnabled(true);
                }

                handler.postDelayed(this, 1000);
            }
        };
        handler.postDelayed(runnable, 500);
    }
    private void initBtns() {
        btn_v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = btn_v.getText().toString();
                if ("请选择视频".equals(s)) {
                    chooseVideo();
                    btn_v.setText("重新选择视频");
                } else {
                    chooseVideo();
                }
                if (mSelectedVideo != null && mSelectedImage != null) {
                    btn_p.setText("现在就上传！");
                    btn_p.setEnabled(true);
                }
            }
        });
        btn_i.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = btn_i.getText().toString();
                if ("请选择封面".equals(s)) {
                    chooseImage();
                    btn_i.setText("重新选择封面");
                } else {
                    chooseImage();
                }
                if (mSelectedVideo != null && mSelectedImage != null) {
                    btn_p.setText("现在就上传！");
                }
            }
        });
        btn_p.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = btn_p.getText().toString();
                if (mSelectedVideo != null && mSelectedImage != null) {
                    postVideo();
                    btn_p.setText("待选择完成");
                    btn_p.setEnabled(false);
                } else {
                    Toast.makeText(PostActivity.this,"请先选择封面与视频！",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "选择封面"),
                PICK_IMAGE);
    }

    public void chooseVideo() {
        Intent intent = new Intent();
        intent.setType("video/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "选择视频"), PICK_VIDEO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult() called with: requestCode = ["
                + requestCode
                + "], resultCode = ["
                + resultCode
                + "], data = ["
                + data
                + "]");
        if (resultCode == RESULT_OK && null != data) {
            if (requestCode == PICK_IMAGE) {
                mSelectedImage = data.getData();
                Log.d(TAG, "selectedImage = " + mSelectedImage);
            } else if (requestCode == PICK_VIDEO) {
                mSelectedVideo = data.getData();
                Log.d(TAG, "mSelectedVideo = " + mSelectedVideo);
            }
        }
    }

    private MultipartBody.Part getMultipartFromUri(String name, Uri uri) {
        File f = new File(ResourceUtils.getRealPath(PostActivity.this, uri));
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), f);
        return MultipartBody.Part.createFormData(name, f.getName(), requestFile);
    }

    private void postVideo() {
        btn_p.setText("正在上传…");

        MultipartBody.Part coverImagePart = getMultipartFromUri("cover_image", mSelectedImage);
        MultipartBody.Part videoPart = getMultipartFromUri("video", mSelectedVideo);
        miniDouyinService.postvideo("3170105259","张庆春",coverImagePart,videoPart).enqueue(
                new Callback<PostVideoResponse>()
                {
                    @Override
                    public void onResponse(Call<PostVideoResponse> call, Response<PostVideoResponse> response) {
                        if(response.body()!=null)
                        {
                            if(response.body().success==true)
                            {
                                Toast.makeText(PostActivity.this, "上传成功！", Toast.LENGTH_SHORT).show();
                            }
                            btn_p.setText("待选择完成");
                            btn_i.setText("请选择封面");
                            btn_v.setText("请选择视频");
                        }
                        else
                        {
                            Toast.makeText(PostActivity.this, "上传失败！", Toast.LENGTH_SHORT).show();
                            btn_p.setText("待选择完成");
                            btn_i.setText("请选择封面");
                            btn_v.setText("请选择视频");;
                        }
                    }

                    @Override
                    public void onFailure(Call<PostVideoResponse> call, Throwable throwable) {
                        btn_p.setText("待选择完成");
                        btn_i.setText("请选择封面");
                        btn_v.setText("请选择视频");;
                        Toast.makeText(PostActivity.this, "上传失败2！", Toast.LENGTH_SHORT).show();
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

}
