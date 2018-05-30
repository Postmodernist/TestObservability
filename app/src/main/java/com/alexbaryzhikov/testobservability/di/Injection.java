package com.alexbaryzhikov.testobservability.di;

import com.alexbaryzhikov.testobservability.data.Repository;
import com.alexbaryzhikov.testobservability.ui.ViewModelFactory;

public class Injection {

  public static ViewModelFactory provideViewModelFactory() {
    Repository repository = provideRepository();
    return new ViewModelFactory(repository);
  }

  private static Repository provideRepository() {
    return new Repository();
  }
}
