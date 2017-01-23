package com.craig.couchbasesyncexample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements ReplicationListener {

    private static final String TAG = MainActivity.class.getName();
    private AsyncWorker asyncWorker;
    private MainController mainController = new MainController();
    private TextView txtCount;
    private TextView txtStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txtCount = (TextView) findViewById(R.id.txtCount);
        txtStatus = (TextView) findViewById(R.id.txtStatus);
        DependencyInjectorFactory.instance().inject(this);
    }

    public void onGo(View sender) {
        Log.d(TAG, "Create button clicked.");
        getAsyncWorker().doAsync(new Runnable(){
            @Override
            public void run() {
                getMainController().onGo();
            }
        });
    }

    public void onSync(View sender) {
        Log.d(TAG, "Sync button clicked.");
        getAsyncWorker().doAsync(new Runnable(){
            @Override
            public void run() {
                getMainController().onSync();
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

    @Override
    public void updatedDocumentCount(final int update) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                txtCount.setText(String.valueOf(update));
            }
        });
    }

    @Override
    public void status(final String status) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                txtStatus.setText(status);
            }
        });
    }
}
