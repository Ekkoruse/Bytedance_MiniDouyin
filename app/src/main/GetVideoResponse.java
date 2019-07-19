package com.example.minidou3.api;

import com.example.minidou3.model.Video;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetVideoResponse {
    @SerializedName("feeds") public List<Video> video;
    @SerializedName("success") public boolean success;


    public List<Video> getVideo(){return video;}



}
