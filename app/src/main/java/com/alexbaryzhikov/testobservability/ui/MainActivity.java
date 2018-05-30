package com.alexbaryzhikov.testobservability.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.alexbaryzhikov.testobservability.R;
import com.alexbaryzhikov.testobservability.data.MyModel;
import com.alexbaryzhikov.testobservability.di.Injection;

import io.reactivex.disposables.Disposable;

public class MainActivity extends AppCompatActivity {

  private static final String TAG = "MainActivity";

  private MyViewModel viewModel;
  private Disposable disposable;

  private TextView statusView;
  private TextView dataView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    statusView = findViewById(R.id.status);
    dataView = findViewById(R.id.data);
    Button getNormalButton = findViewById(R.id.get_normal);
    Button getCorruptButton = findViewById(R.id.get_corrupt);
    Button getErrorButton = findViewById(R.id.get_error);

    // Get view model
    ViewModelFactory viewModelFactory = Injection.provideViewModelFactory();
    viewModel = ViewModelProviders.of(this, viewModelFactory).get(MyViewModel.class);

    // Subscribe to incoming stream
    disposable = viewModel.getObservable().subscribe(this::render, this::error);

    // Setup buttons
    getNormalButton.setOnClickListener(v -> viewModel.onClick("Hello world!"));
    getCorruptButton.setOnClickListener(v -> viewModel.onClick("Rogue bytes"));
    getErrorButton.setOnClickListener(v -> viewModel.onClick("Erroneous"));
  }

  @Override
  protected void onStop() {
    super.onStop();
    disposable.dispose();
  }

  private void render(Resource<MyModel> resource) {
    String status = getString(R.string.status, resource.getStatus().name());
    String data = getString(R.string.data,
        resource.getData() != null ? resource.getData().getId() : "N/A");

    statusView.setText(status);
    dataView.setText(data);
  }

  private void error(Throwable throwable) {
    Log.e(TAG, "error: Stream malfunction", throwable);
    this.finishAffinity();
  }
}
