package net.protoblogr.devfest.handytuner;

import android.app.Activity;
import android.os.Bundle;
import android.support.wearable.view.WatchViewStub;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.HashMap;

public class LaunchActivity extends Activity {

    private TextView riTextView;
    Thread recorder_thread_;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);
        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {
                riTextView = (TextView) stub.findViewById(R.id.recorder_info);
            }
        });

         //, new Handler()
    }

    public void onStartRecord(View view) {
        Log.d("View", "onStartRecord");
        recorder_thread_ = new Thread(new Recorder(this));
        recorder_thread_.start();
    }
    public void onStopRecord(View view) {
        Log.d("View", "onStopRecord");

        recorder_thread_.interrupt();
    }

    public void ShowRecorderDetectionResult(String message, HashMap<Double, Double> frequencies) {
        // TODO: implement UI update
        //Log.d("ShowRecorderDetectionResult", message);
        riTextView.setText(message);
    }
}
