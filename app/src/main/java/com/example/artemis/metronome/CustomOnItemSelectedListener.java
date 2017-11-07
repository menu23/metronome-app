package com.example.artemis.metronome;

import android.media.MediaPlayer;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Spinner;


public class CustomOnItemSelectedListener implements OnItemSelectedListener {

    Spinner spinner;
    MediaPlayer mediaPlayer;

    public void onItemSelected(AdapterView<?> parent, View view, int pos,long id) {
        // something
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // nothing
    }
}