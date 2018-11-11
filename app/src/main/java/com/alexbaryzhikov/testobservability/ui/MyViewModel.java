package com.alexbaryzhikov.testobservability.ui;

import com.alexbaryzhikov.testobservability.data.MyDataModel;
import com.alexbaryzhikov.testobservability.data.Repository;

import androidx.lifecycle.ViewModel;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;

class MyViewModel extends ViewModel {
    private static final Resource<MyDataModel> RESOURCE_LOADING = new Resource.Loading<>();

    private final Repository repository;
    private final PublishSubject<String> clickSubject = PublishSubject.create();

    MyViewModel(Repository repository) {
        this.repository = repository;
    }

    Observable<Resource<MyDataModel>> getObservable() {
        return Observable
                .switchOnNext(clickSubject.map(s -> repository.getMyModel(s)
                        .toObservable()
                        .map(MyViewModel::toResource)
                        .startWith(RESOURCE_LOADING)))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    void onClick(String modelId) {
        clickSubject.onNext(modelId);
    }

    private static Resource<MyDataModel> toResource(MyDataModel myDataModel) {
        return MyDataModel.CORRUPT_ID.equals(myDataModel.getId()) ?
                new Resource.Failure<>(myDataModel) : new Resource.Success<>(myDataModel);
    }
}
