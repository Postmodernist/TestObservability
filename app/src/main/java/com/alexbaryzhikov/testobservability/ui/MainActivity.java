package com.alexbaryzhikov.testobservability.ui;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.alexbaryzhikov.testobservability.R;
import com.alexbaryzhikov.testobservability.data.MyDataModel;
import com.alexbaryzhikov.testobservability.di.Injection;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
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

        // Setup buttons
        getNormalButton.setOnClickListener(v -> viewModel.onClick(MyDataModel.NORMAL_ID));
        getCorruptButton.setOnClickListener(v -> viewModel.onClick(MyDataModel.CORRUPT_ID));
        getErrorButton.setOnClickListener(v -> viewModel.onClick(MyDataModel.STREAM_ERROR_ID));
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Subscribe to incoming stream
        disposable = viewModel.getObservable().subscribe(this::onNext, this::onError);
        Log.i(TAG, "Started observing resource stream");
    }

    @Override
    protected void onStop() {
        super.onStop();
        disposable.dispose();
        Log.i(TAG, "Finished observing resource stream");
    }

    public void onNext(Resource<MyDataModel> resource) {
        Log.i(TAG, "Received " + resource);
        String status = getString(R.string.status, resource.getClass().getSimpleName());
        String data = resource.getResult() != null ?
                getString(R.string.data, resource.getResult().getId()) : getString(R.string.data_na);

        statusView.setText(status);
        dataView.setText(data);
    }

    public void onError(Throwable e) {
        Log.e(TAG, "Stream error", e);
        this.finishAffinity();
    }
}
