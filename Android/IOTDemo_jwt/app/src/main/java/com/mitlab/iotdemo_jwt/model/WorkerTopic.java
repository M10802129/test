package com.mitlab.iotdemo_jwt.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class WorkerTopic {
    @Expose
    @SerializedName("id")
    int id;

    @Expose
    @SerializedName("worker_type_id")
    long worker_type_id;

    @Expose
    @SerializedName("topic_name")
    String topic_name;

    @Expose
    @SerializedName("topic_path")
    String topic_path;

    @Expose
    @SerializedName("topic_description")
    String topic_description;
}
