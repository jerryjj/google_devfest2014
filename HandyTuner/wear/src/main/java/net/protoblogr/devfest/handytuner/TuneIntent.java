package net.protoblogr.devfest.handytuner;

import android.app.Activity;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.wearable.view.WatchViewStub;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by jja on 06/10/14.
 */
public class TuneIntent extends Activity {
    private static String TAG = "TuneIntent";

    private TextView riTextView;
    private Button e4_btn;
    private Button b3_btn;
    private Button g3_btn;
    private Button d3_btn;
    private Button a2_btn;
    private Button e2_btn;

    Thread recorder_thread_;

    private String curr_note;
    Recorder _recorder;

    Tunes _tunes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rect_activity_tune_intent);

        riTextView = (TextView) findViewById(R.id.recorder_info);
        e4_btn = (Button) findViewById(R.id.find_e4_button);
        b3_btn = (Button) findViewById(R.id.find_b3_button);
        g3_btn = (Button) findViewById(R.id.find_g3_button);
        d3_btn = (Button) findViewById(R.id.find_d3_button);
        a2_btn = (Button) findViewById(R.id.find_a2_button);
        e2_btn = (Button) findViewById(R.id.find_e2_button);

        View.OnClickListener btn_listener = new View.OnClickListener()
        {
            public void onClick(View v) {
                handleNoteClick(v);
            }
        };
        e4_btn.setOnClickListener(btn_listener);
        b3_btn.setOnClickListener(btn_listener);
        g3_btn.setOnClickListener(btn_listener);
        d3_btn.setOnClickListener(btn_listener);
        a2_btn.setOnClickListener(btn_listener);
        e2_btn.setOnClickListener(btn_listener);

        _recorder = new Recorder(this);
        _tunes = new Tunes();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    @Override
    protected void onStop() {
        Log.d(TAG, "onStop");
        if (_recorder.running) {
            _recorder.running = false;
            recorder_thread_.interrupt();
        }
        super.onStop();
    }

    public void handleNoteClick(View view) {
        String previous_note = curr_note;

        switch(view.getId()) {
            case R.id.find_e4_button:
                curr_note = "E4";
                break;
            case R.id.find_b3_button:
                curr_note = "B3";
                break;
            case R.id.find_g3_button:
                curr_note = "G3";
                break;
            case R.id.find_d3_button:
                curr_note = "D3";
                break;
            case R.id.find_a2_button:
                curr_note = "A2";
                break;
            case R.id.find_e2_button:
                curr_note = "E2";
                break;
        }

        Log.d(TAG, "handleNoteClick: "+curr_note);
        if (previous_note == curr_note) {
            _recorder.running = false;
            recorder_thread_.interrupt();
            view.getBackground().setColorFilter(0xCCCCCC, PorterDuff.Mode.SCREEN);
            return;
        }

        recorder_thread_ = new Thread(_recorder);
        recorder_thread_.start();
        view.getBackground().setColorFilter(0x000000, PorterDuff.Mode.MULTIPLY);
    }

    public void ShowRecorderDetectionResult(String message, final double best_frequency, HashMap<Double, Double> frequencies) {
        //Log.d("ShowRecorderDetectionResult", message);

        HashMap<String, Double> closest = _tunes.getClosestPitchNotationByHertz(best_frequency);
        String closest_note = null;
        Double closest_diff = null;
        if (closest != null) {
            Iterator<Map.Entry<String, Double>> cit = closest.entrySet().iterator();
            while(cit.hasNext()) {
                Map.Entry<String, Double> entry = cit.next();
                closest_note = entry.getKey();
                closest_diff = entry.getValue();
            }
            Log.d(TAG, "closest note: "+closest_note);
            Log.d(TAG, "closest diff: "+closest_diff);
        }

        if (curr_note != closest_note) {
            riTextView.setText(message);
            return;
        }

        _recorder.running = false;
        recorder_thread_.interrupt();

        Log.d(TAG, "got current note "+curr_note+" in range: "+closest_diff);
        riTextView.setText("Got "+closest_note);

        if (closest_note == "E4") {
            e4_btn.getBackground().setColorFilter(0xFF00FF00, PorterDuff.Mode.MULTIPLY);
        } else if (closest_note == "B3") {
            b3_btn.getBackground().setColorFilter(0xFF00FF00, PorterDuff.Mode.MULTIPLY);
        } else if (closest_note == "G3") {
            g3_btn.getBackground().setColorFilter(0xFF00FF00, PorterDuff.Mode.MULTIPLY);
        } else if (closest_note == "D3") {
            d3_btn.getBackground().setColorFilter(0xFF00FF00, PorterDuff.Mode.MULTIPLY);
        } else if (closest_note == "A2") {
            a2_btn.getBackground().setColorFilter(0xFF00FF00, PorterDuff.Mode.MULTIPLY);
        } else if (closest_note == "E2") {
            e2_btn.getBackground().setColorFilter(0xFF00FF00, PorterDuff.Mode.MULTIPLY);
        }
    }
}
