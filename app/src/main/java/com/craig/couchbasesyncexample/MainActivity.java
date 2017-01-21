package com.craig.couchbasesyncexample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getName();
    private AsyncWorker asyncWorker;
    private MainController mainController = new MainController();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DependencyInjectorFactory.instance().inject(this);
    }

    public void onGo(View sender) {
        Log.d(TAG, "Go button clicked.");
        getAsyncWorker().doAsync(new Runnable(){
            @Override
            public void run() {
                getMainController().onGo();
            }
        });
    }

    public AsyncWorker getAsyncWorker() {
        return asyncWorker;
    }

    public void setAsyncWorker(AsyncWorker asyncWorker) {
        this.asyncWorker = asyncWorker;
    }

    public MainController getMainController() {
        return mainController;
    }
}
