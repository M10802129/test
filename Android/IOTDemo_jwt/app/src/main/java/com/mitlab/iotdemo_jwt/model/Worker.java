package com.mitlab.iotdemo_jwt.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Worker {
    @Expose
    @SerializedName("id")
    int id;

    @Expose
    @SerializedName("user_id")
    int user_id;

    @Expose
    @SerializedName("name")
    String name;

    @Expose
    @SerializedName("worker_type_id")
    long worker_type_id;

    @Expose
    @SerializedName("description")
    String description;

    @Expose
    @SerializedName("worker_type_info")
    WorkerTypeInfo workerTypeInfo;

    @Expose
    @SerializedName("topics")
    WorkerTopic[] workerTopics;

    public String getName(){ return name; }
}
