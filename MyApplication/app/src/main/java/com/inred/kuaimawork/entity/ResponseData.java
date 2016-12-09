package com.inred.kuaimawork.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by inred on 2016/10/26.
 */
public class ResponseData<T> {

    @SerializedName("data")
    public T data;
    @SerializedName("state")
    public State state;

    public static class State {

        @SerializedName("code")
        public int code;
        @SerializedName("errorMsg")
        public String message;
    }

}
