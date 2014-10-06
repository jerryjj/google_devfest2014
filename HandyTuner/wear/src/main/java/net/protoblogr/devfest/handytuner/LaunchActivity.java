package net.protoblogr.devfest.handytuner;

import android.content.Intent;
import android.os.Bundle;
import android.support.wearable.activity.InsetActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class LaunchActivity extends InsetActivity {
    private static String TAG = "LaunchActivity";

    private View _main_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onReadyForContent() {
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
        _main_view  = inflater.inflate(R.layout.rect_activity_start, null);
        setContentView(_main_view);
        Button tune_btn = (Button) _main_view.findViewById(R.id.tune_btn);
        OnClickListener btn_listener = new OnClickListener()
        {
            @Override
            public void onClick(View v) {
                Intent tuneIntent = new Intent(LaunchActivity.this, TuneIntent.class);
                LaunchActivity.this.startActivity(tuneIntent);
            }
        };
        tune_btn.setOnClickListener(btn_listener);
    }
}