package com.mitlab.iotdemo_jwt.network.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PositionSetResponse {
    @Expose
    @SerializedName("success")
    boolean success;

    public boolean getSuccess() {
        return success;
    }

    public void setError(boolean success) {
        this.success = success;
    }
}
