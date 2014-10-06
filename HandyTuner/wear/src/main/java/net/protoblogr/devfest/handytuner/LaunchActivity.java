package net.protoblogr.devfest.handytuner;

import android.app.Activity;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.wearable.view.WatchViewStub;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

public class LaunchActivity extends Activity {

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
        setContentView(R.layout.activity_launch);

        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {
            riTextView = (TextView) stub.findViewById(R.id.recorder_info);
            e4_btn = (Button) stub.findViewById(R.id.find_e4_button);
            b3_btn = (Button) stub.findViewById(R.id.find_b3_button);
            g3_btn = (Button) stub.findViewById(R.id.find_g3_button);
            d3_btn = (Button) stub.findViewById(R.id.find_d3_button);
            a2_btn = (Button) stub.findViewById(R.id.find_a2_button);
            e2_btn = (Button) stub.findViewById(R.id.find_e2_button);

            OnClickListener btn_listener = new OnClickListener()
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
            }
        });
        _recorder = new Recorder(this);
        _tunes = new Tunes();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        //displaySpeechRecognizer();

        /*
        // 329.50, 246.40
        HashMap<String, Double> closest = _tunes.getClosestPitchNotationByHertz(246.40);
        if (closest != null) {
            Iterator<Entry<String, Double>> cit = closest.entrySet().iterator();
            while(cit.hasNext()) {
                Entry<String, Double> entry = cit.next();
                String note = entry.getKey();
                Double diff = entry.getValue();
                Log.d("Tunes", "closest note: "+note);
                Log.d("Tunes", "closest diff: "+diff);
            }
        } else {
            Log.d("Tunes", "no match found");
        }*/
    }

    @Override
    protected void onStop() {
        Log.d("Activity", "onStop");
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

        Log.d("View", "handleNoteClick: "+curr_note);
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
    /*
    public void onFindE4(View view) {
        Log.d("View", "onFindE4");
        curr_note = "E4";
        recorder_thread_ = new Thread(_recorder);
        recorder_thread_.start();
        e4_btn.getBackground().setColorFilter(0x000000, PorterDuff.Mode.MULTIPLY);
    }
    public void onFindB3(View view) {
        Log.d("View", "onFindB3");
        curr_note = "B3";
        recorder_thread_ = new Thread(_recorder);
        recorder_thread_.start();
        b3_btn.getBackground().setColorFilter(0x000000, PorterDuff.Mode.MULTIPLY);
    }
    public void onFindG3(View view) {
        Log.d("View", "onFindG3");
        curr_note = "G3";
        recorder_thread_ = new Thread(_recorder);
        recorder_thread_.start();
        g3_btn.getBackground().setColorFilter(0x000000, PorterDuff.Mode.MULTIPLY);
    }
    public void onFindD3(View view) {
        Log.d("View", "onFindD3");
        curr_note = "D3";
        recorder_thread_ = new Thread(_recorder);
        recorder_thread_.start();
        d3_btn.getBackground().setColorFilter(0x000000, PorterDuff.Mode.MULTIPLY);
    }
    public void onFindA2(View view) {
        Log.d("View", "onFindA2");
        curr_note = "A2";
        recorder_thread_ = new Thread(_recorder);
        recorder_thread_.start();
        a2_btn.getBackground().setColorFilter(0x000000, PorterDuff.Mode.MULTIPLY);
    }
    public void onFindE2(View view) {
        Log.d("View", "onFindE2");
        curr_note = "E2";
        recorder_thread_ = new Thread(_recorder);
        recorder_thread_.start();
        e2_btn.getBackground().setColorFilter(0x000000, PorterDuff.Mode.MULTIPLY);
    }*/

    public void ShowRecorderDetectionResult(String message, final double best_frequency, HashMap<Double, Double> frequencies) {
        //Log.d("ShowRecorderDetectionResult", message);

        HashMap<String, Double> closest = _tunes.getClosestPitchNotationByHertz(best_frequency);
        String closest_note = null;
        Double closest_diff = null;
        if (closest != null) {
            Iterator<Entry<String, Double>> cit = closest.entrySet().iterator();
            while(cit.hasNext()) {
                Entry<String, Double> entry = cit.next();
                closest_note = entry.getKey();
                closest_diff = entry.getValue();
            }
            Log.d("Tunes", "closest note: "+closest_note);
            Log.d("Tunes", "closest diff: "+closest_diff);
        }

        if (curr_note != closest_note) {
            riTextView.setText(message);
            return;
        }

        _recorder.running = false;
        recorder_thread_.interrupt();

        Log.d("ShowRecorderDetectionResult", "got current note "+curr_note+" in range: "+closest_diff);
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

        /*
        if (curr_note == "E4") {
            if (best_frequency > 329.00 && best_frequency < 340.00) {
                _recorder.running = false;
                recorder_thread_.interrupt();
                //recorder_thread_ = null;

                Log.d("show", "got in E4 range");
                riTextView.setText("Got E4");

                e4_btn.getBackground().setColorFilter(0xFF00FF00, PorterDuff.Mode.MULTIPLY);
                return;
            }
        } else if (curr_note == "B3") {
            if (best_frequency > 246.00 && best_frequency < 247.00) {
                _recorder.running = false;
                recorder_thread_.interrupt();
                //recorder_thread_ = null;

                Log.d("show", "got in B3 range");
                riTextView.setText("Got B3");
                b3_btn.getBackground().setColorFilter(0xFF00FF00, PorterDuff.Mode.MULTIPLY);
                return;
            }
        } else if (curr_note == "G3") {
            //Log.d("best_frequency", ""+best_frequency);
            //if (best_frequency > 195.00 && best_frequency < 197.00) {
            if ((best_frequency > 589.00 && best_frequency < 590.00) || (best_frequency > 195.00 && best_frequency < 197.00)) {
                _recorder.running = false;
                recorder_thread_.interrupt();
                //recorder_thread_ = null;

                Log.d("show", "got in G3 range");
                riTextView.setText("Got G3");
                g3_btn.getBackground().setColorFilter(0xFF00FF00, PorterDuff.Mode.MULTIPLY);
                return;
            }
        } else if (curr_note == "D3") {
            //Log.d("best_frequency", ""+best_frequency);
            //if (best_frequency > 146.00 && best_frequency < 147.00) {
            if (best_frequency > 441.00 && best_frequency < 442.00) {
                _recorder.running = false;
                recorder_thread_.interrupt();
                //recorder_thread_ = null;

                Log.d("show", "got in D3 range");
                riTextView.setText("Got D3");
                d3_btn.getBackground().setColorFilter(0xFF00FF00, PorterDuff.Mode.MULTIPLY);
                return;
            }
        } else if (curr_note == "A2") {
            //Log.d("best_frequency", ""+best_frequency);
            if (best_frequency > 109.80 && best_frequency < 110.90) {
                _recorder.running = false;
                recorder_thread_.interrupt();
                //recorder_thread_ = null;

                Log.d("show", "got in A2 range");
                riTextView.setText("Got A2");
                a2_btn.getBackground().setColorFilter(0xFF00FF00, PorterDuff.Mode.MULTIPLY);
                return;
            }
        } else if (curr_note == "E2") {
            //Log.d("best_frequency", ""+best_frequency);
            if (best_frequency > 82.00 && best_frequency < 82.90) {
                _recorder.running = false;
                recorder_thread_.interrupt();
                //recorder_thread_ = null;

                Log.d("show", "got in E2 range");
                riTextView.setText("Got E2");
                e2_btn.getBackground().setColorFilter(0xFF00FF00, PorterDuff.Mode.MULTIPLY);
                return;
            }
        }
        */
        //riTextView.setText(message);

        /*
        final int MaxAmplitude = 500;
        Iterator<Entry<Double, Double>> it = frequencies.entrySet().iterator();

        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        final int bounding_top = 50;
        final int bounding_bottom = stub.getHeight() - 50;

        while(it.hasNext()) {
            Entry<Double, Double> entry = it.next();
            double frequency = entry.getKey();
            double amplitude = Math.min(entry.getValue(), MaxAmplitude);
            long height = Math.round(amplitude / MaxAmplitude * (bounding_bottom - bounding_top));
            Log.d("frequency", ""+frequency);
            Log.d("real amplitude", ""+entry.getValue());
            Log.d("amplitude", ""+amplitude);
            Log.d("Height", ""+height);
        }
        */
    }
    /*
    private static final int SPEECH_REQUEST_CODE = 0;

    // Create an intent that can start the Speech Recognizer activity
    private void displaySpeechRecognizer() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
// Start the activity, the intent will be populated with the speech text
        startActivityForResult(intent, SPEECH_REQUEST_CODE);
    }

    // This callback is invoked when the Speech Recognizer returns.
// This is where you process the intent and extract the speech text from the intent.
    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        Log.d("App", "onActivityResult");
        if (requestCode == SPEECH_REQUEST_CODE && resultCode == RESULT_OK) {
            List<String> results = data.getStringArrayListExtra(
                    RecognizerIntent.EXTRA_RESULTS);
            String spokenText = results.get(0);
            Log.d("spokenText", spokenText);
            // Do something with spokenText
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    */
}
