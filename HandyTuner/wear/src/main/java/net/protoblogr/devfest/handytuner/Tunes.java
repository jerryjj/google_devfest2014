package net.protoblogr.devfest.handytuner;

import android.util.Log;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Created by jja on 05/10/14.
 */
public class Tunes {
    HashMap<String, Double> _registry = new HashMap<String, Double>();

    private Double MAX_DIFF = 0.50;

    public Tunes () {
        _registerNotes();
    }

    private void _registerNotes() {
        _registry.put("E4", 329.63);
        _registry.put("B3", 246.94);
        _registry.put("G3", 196.00);
        _registry.put("D3", 146.83);
        _registry.put("A2", 110.00);
        _registry.put("E2", 82.41);
    }

    public HashMap<String, Double> getClosestPitchNotationByHertz(Double hertz) {
        Iterator<Map.Entry<String, Double>> it = _registry.entrySet().iterator();
        while(it.hasNext()) {
            Entry<String, Double> entry = it.next();
            String n = entry.getKey();
            Double h = entry.getValue();
            HashMap<String, Double> res = new HashMap<String, Double>();
            Double diff = (hertz - h);
            Double rdiff = (h - hertz);
            /*
            Log.d("Tuner", "hertz: "+hertz);
            Log.d("Tuner", "n: "+n);
            Log.d("Tuner", "h: "+h);
            Log.d("Tuner", "diff: "+diff);
            Log.d("Tuner", "rdiff: "+rdiff);
            */
            if (-MAX_DIFF <= diff && diff <= MAX_DIFF) {
                res.put(n, diff);
                return res;
            } else if (rdiff <= MAX_DIFF && rdiff >= -MAX_DIFF) {
                res.put(n, rdiff);
                return res;
            }
        }

        return null;
    }

    public Double pitchNotationToHertz(String notation) {
        return _registry.get(notation);
    }
}
