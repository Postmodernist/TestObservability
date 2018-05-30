package com.alexbaryzhikov.testobservability.ui;

import android.arch.lifecycle.ViewModel;

import com.alexbaryzhikov.testobservability.data.MyModel;
import com.alexbaryzhikov.testobservability.data.Repository;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;

public class MyViewModel extends ViewModel {

  private static final Resource<MyModel> resourceLoading = Resource.loading((MyModel) null);

  private final Repository repository;
  private final PublishSubject<String> clickSubject = PublishSubject.create();

  MyViewModel(Repository repository) {
    this.repository = repository;
  }

  private static Resource<MyModel> toResource(MyModel myModel) {
    return "Rogue bytes".equals(myModel.getId()) ? Resource.error(myModel) : Resource.success(myModel);
  }

  public Observable<Resource<MyModel>> getObservable() {
    return Observable
        .switchOnNext(clickSubject
            .map(s -> repository.getMyModel(s)
                .toObservable()
                .map(MyViewModel::toResource)
                .startWith(resourceLoading)))
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread());
  }

  public void onClick(String modelId) {
    clickSubject.onNext(modelId);
  }
}
