package com.alexbaryzhikov.testobservability.data;

public class MyDataModel {
    public static final String NORMAL_ID = "Hello stream!";
    public static final String CORRUPT_ID = "Rogue bytes";
    public static final String STREAM_ERROR_ID = "Erroneous";

    private final String id;

    MyDataModel(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
