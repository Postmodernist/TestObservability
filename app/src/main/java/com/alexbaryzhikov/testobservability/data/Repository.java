package com.alexbaryzhikov.testobservability.data;

import java.util.concurrent.TimeUnit;

import io.reactivex.Single;

public class Repository {

  public Single<MyModel> getMyModel(String modelId) {
    return Single.fromCallable(() -> new MyModel(modelId))
        // Imitate time-consuming operation
        .delay(3, TimeUnit.SECONDS)
        // Imitate error
        .map(myModel -> {
          if ("Erroneous".equals(myModel.getId())) {
            throw new RuntimeException("Totally unexpected exception");
          }
          return myModel;
        });
  }
}
