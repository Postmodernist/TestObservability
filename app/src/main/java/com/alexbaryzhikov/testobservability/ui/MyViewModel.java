package com.alexbaryzhikov.testobservability.ui;

import android.arch.lifecycle.ViewModel;

import com.alexbaryzhikov.testobservability.data.MyModel;
import com.alexbaryzhikov.testobservability.data.Repository;

import io.reactivex.Flowable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class MyViewModel extends ViewModel {

  private final Repository repository;

  MyViewModel(Repository repository) {
    this.repository = repository;
  }

  public Flowable<Resource<MyModel>> requestData(String modelId) {
    return Single.just(Resource.loading((MyModel) null))
        .concatWith(repository.getMyModel(modelId)
            .map(myModel -> "Rogue bytes".equals(myModel.getId()) ?
                Resource.error(myModel) : Resource.success(myModel)))
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread());
  }
}
