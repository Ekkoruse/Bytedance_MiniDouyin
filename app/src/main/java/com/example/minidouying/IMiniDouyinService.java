package com.example.minidouying;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface IMiniDouyinService {
    // TODO 7: Define IMiniDouyinService

    String HOST = "http://test.androidcamp.bytedance.com/mini_douyin/invoke/";
    String PATH = "video";

    @Multipart
    @POST(PATH)
    Call<PostVideoResponse> postvideo(
            @Query("student_id") String student_id,
            @Query("user_name") String user_name,
            @Part MultipartBody.Part image,
            @Part MultipartBody.Part video
    );

    @GET(PATH)
    Call<GetVideoResponse> getvideos();

}

