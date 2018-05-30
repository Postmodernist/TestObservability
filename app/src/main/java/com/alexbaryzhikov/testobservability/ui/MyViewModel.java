package com.alexbaryzhikov.testobservability.ui;

import android.arch.lifecycle.ViewModel;

import com.alexbaryzhikov.testobservability.data.MyModel;
import com.alexbaryzhikov.testobservability.data.Repository;

import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class MyViewModel extends ViewModel {

  private final Repository repository;
  private SingleEmitter<String> trigger;

  MyViewModel(Repository repository) {
    this.repository = repository;
  }

  public Single<String> getMyModelData(String modelId) {

    // Fire the trigger to cancel the previous loading
    if (trigger != null) {
      trigger.onSuccess("Fire");
    }

    // Setup cancellation trigger for current stream
    Single<String> controller = Single.create(emitter -> trigger = emitter);

    return repository.getMyModel(modelId)
        .map(MyModel::getId)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .takeUntil(controller);
  }
}
