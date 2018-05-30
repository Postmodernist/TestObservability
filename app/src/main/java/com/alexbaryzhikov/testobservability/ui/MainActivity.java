package com.alexbaryzhikov.testobservability.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.alexbaryzhikov.testobservability.R;
import com.alexbaryzhikov.testobservability.di.Injection;

import java.util.concurrent.CancellationException;

import io.reactivex.SingleObserver;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public class MainActivity extends AppCompatActivity {

  private static final String TAG = "MainActivity";

  private final CompositeDisposable disposables = new CompositeDisposable();
  private MyViewModel viewModel;

  private TextView statusView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    statusView = findViewById(R.id.status);
    Button getButton = findViewById(R.id.get_normal);
    Button getErrorButton = findViewById(R.id.get_error);

    // Get view model
    ViewModelFactory viewModelFactory = Injection.provideViewModelFactory();
    viewModel = ViewModelProviders.of(this, viewModelFactory).get(MyViewModel.class);

    // Setup buttons
    getButton.setOnClickListener(v -> subscribeTo("Hello world!"));
    getErrorButton.setOnClickListener(v -> subscribeTo("Erroneous"));
  }

  @Override
  protected void onStart() {
    super.onStart();
    render(getString(R.string.status_idle));
  }

  @Override
  protected void onStop() {
    super.onStop();
    disposables.clear();
  }

  private void subscribeTo(String modelId) {
    viewModel.getMyModelData(modelId)
        .subscribe(new SingleObserver<String>() {
          @Override
          public void onSubscribe(Disposable d) {
            disposables.add(d);
            render(getString(R.string.status_loading));
          }

          @Override
          public void onSuccess(String s) {
            render(getString(R.string.result_success, s));
          }

          @Override
          public void onError(Throwable e) {
            if (e instanceof CancellationException) {
              Log.d(TAG, "onError: Loading aborted");
            } else {
              render(getString(R.string.result_error));
              Log.e(TAG, "onError: Unable to get data", e);
            }
          }
        });
  }

  private void render(String msg) {
    statusView.setText(msg);
  }
}
