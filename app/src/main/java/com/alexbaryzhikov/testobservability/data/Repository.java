package com.alexbaryzhikov.testobservability.data;

import android.util.Log;

import java.util.concurrent.TimeUnit;

import io.reactivex.Single;

public class Repository {
    private static final String TAG = "Repository";

    public Single<MyDataModel> getMyModel(final String modelId) {
        Log.i(TAG, "Fetching resource with id=" + modelId);
        return Single.just(new MyDataModel(modelId))
                // Imitate time-consuming operation
                .delay(3, TimeUnit.SECONDS)
                // Imitate stream error
                .map(myDataModel -> {
                    if (MyDataModel.STREAM_ERROR_ID.equals(modelId)) {
                        throw new RuntimeException("Horrible stream error!");
                    }
                    return myDataModel;
                });
    }
}
