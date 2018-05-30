package com.alexbaryzhikov.testobservability.ui;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.alexbaryzhikov.testobservability.data.Repository;

public class ViewModelFactory implements ViewModelProvider.Factory {

  private final Repository repository;

  public ViewModelFactory(Repository repository) {
    this.repository = repository;
  }

  @NonNull
  @Override
  public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
    if (modelClass.isAssignableFrom(MyViewModel.class)) {
      // noinspection unchecked
      return (T) new MyViewModel(repository);
    }
    throw new IllegalArgumentException("Unknown ViewModel class");
  }
}
