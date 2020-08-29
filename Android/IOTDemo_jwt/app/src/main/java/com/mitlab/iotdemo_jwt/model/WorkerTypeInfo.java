package com.mitlab.iotdemo_jwt.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class WorkerTypeInfo {
    @Expose
    @SerializedName("id")
    int id;
    @Expose
    @SerializedName("name")
    String name;
    @Expose
    @SerializedName("description")
    String description;
}
