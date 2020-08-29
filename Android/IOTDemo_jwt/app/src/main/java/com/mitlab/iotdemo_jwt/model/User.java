package com.mitlab.iotdemo_jwt.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class User {

    @Expose
    @SerializedName("id")
    int id;

    @Expose
    @SerializedName("name")
    String name;

    @Expose
    @SerializedName("email")
    String email;

    @Expose
    @SerializedName("worker")
    Worker worker;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Worker getWorker(){ return worker; }

    public void setWorker(Worker worker) {
        this.worker = worker;
    }
}
