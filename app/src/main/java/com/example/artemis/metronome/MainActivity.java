package com.example.artemis.metronome;

import android.content.Context;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Vibrator;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    int bpmNum;
    EditText bpmText;
    int count=1;
    Timer T;
    MediaPlayer mediaPlayer;
    Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bpmText = (EditText)findViewById(R.id.bpm);
        bpmNum = Integer.parseInt(bpmText.getText().toString());

        Button stopBtnCode = (Button)findViewById(R.id.stopBtn);
        stopBtnCode.setEnabled(false);

        mediaPlayer = MediaPlayer.create(MainActivity.this,R.raw.click);
        addListenerOnSpinnerItemSelection();

        bpmText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {

                    bpmText = (EditText)findViewById(R.id.bpm);
                    bpmNum = Integer.parseInt(bpmText.getText().toString());

                    if (bpmNum > 200) {
                        bpmNum = 200;
                        display();
                        Toast.makeText(getBaseContext(), "Maximum 200 BPM",
                                Toast.LENGTH_SHORT).show();
                    }
                    else if (bpmNum < 40) {
                        bpmNum = 40;
                        display();
                        Toast.makeText(getBaseContext(), "Minimum 40 BPM",
                                Toast.LENGTH_SHORT).show();
                    }

                    tempoDisp();

                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(bpmText.getWindowToken(), 0);

                    return true;
                }
                return false;
            }
        });
    }

    public void addListenerOnSpinnerItemSelection() {
        spinner = (Spinner) findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(new CustomOnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                spinnerAction();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // nothing
            }
        });
    }

    public void spinnerAction() {
        spinner = (Spinner)findViewById(R.id.spinner);
        String sound = String.valueOf(spinner.getSelectedItem());

        if ( sound.equals("Click") )
            mediaPlayer = MediaPlayer.create(MainActivity.this,R.raw.click);
        else if ( sound.equals("Wood") )
            mediaPlayer = MediaPlayer.create(MainActivity.this,R.raw.wood);
        else if ( sound.equals("Metal") )
            mediaPlayer = MediaPlayer.create(MainActivity.this,R.raw.metal);
        else if ( sound.equals("Silent") )
            mediaPlayer = null;
    }

    public void increment(View view)
    {
        bpmText = (EditText)findViewById(R.id.bpm);
        bpmNum = Integer.parseInt(bpmText.getText().toString());

        if (bpmNum < 200 )
            bpmNum++;
        display();
        tempoDisp();
    }

    public void decrement(View view)
    {
        bpmText = (EditText)findViewById(R.id.bpm);
        bpmNum = Integer.parseInt(bpmText.getText().toString());

        if (bpmNum > 40 )
            bpmNum--;
        display();
        tempoDisp();
    }

    public void display() {
        bpmText = (EditText)findViewById(R.id.bpm);
        bpmText.setText(String.valueOf(bpmNum));
    }

    public void tempoDisp() {
        String str;

        if (bpmNum <= 50)
            str = "Grave";
        else if (bpmNum > 50 && bpmNum <= 55)
            str = "Largo";
        else if (bpmNum > 55 && bpmNum <= 60)
            str = "Larghetto";
        else if (bpmNum > 60 && bpmNum <= 70)
            str = "Adagio";
        else if (bpmNum > 70 && bpmNum <= 85)
            str = "Adante";
        else if (bpmNum > 85 && bpmNum <= 100)
            str = "Moderato";
        else if (bpmNum > 100 && bpmNum <= 115)
            str = "Allegretto";
        else if (bpmNum > 115 && bpmNum <= 140)
            str = "Allegro";
        else if (bpmNum > 140 && bpmNum <= 150)
            str = "Vivace";
        else if (bpmNum > 150 && bpmNum <= 170)
            str = "Presto";
        else
            str = "Prestissimo";

        TextView tempoText = (TextView) findViewById(R.id.tempo);
        tempoText.setText(String.valueOf(str));
    }

    public void play(View view) {

        final TextView countText = (TextView) findViewById(R.id.counter);

        bpmText = (EditText)findViewById(R.id.bpm);
        bpmNum = Integer.parseInt(bpmText.getText().toString());

        final long time = ((long)60000)/bpmNum;

        T = new Timer();
        T.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        if ( count == 1 )
                        {
                            countText.setTextColor(Color.parseColor("#d50000"));
                            /*try {
                                mediaPlayer.seekTo(0);
                                mediaPlayer.start();
                            }
                            catch (Exception e) {
                                Log.e("MainActivity",e.getMessage());
                            }*/
                        }
                        else {
                            countText.setTextColor(Color.parseColor("#212121"));
                        }

                        try {
                            if (mediaPlayer != null) {
                                mediaPlayer.seekTo(0);
                                mediaPlayer.start();
                            }
                        }
                        catch (Exception e) {
                            Log.e("MainActivity",e.getMessage());
                        }

                        countText.setText(String.valueOf(count));

                        if (mediaPlayer != null) {
                            Vibrator v = (Vibrator) getBaseContext().getSystemService(Context.VIBRATOR_SERVICE);
                            v.vibrate(time/4);
                        }

                        count++;
                        if ( count == 5 )
                            count = 1;

                        Button stopBtnCode = (Button)findViewById(R.id.stopBtn);
                        stopBtnCode.setEnabled(true);
                        Button playBtnCode = (Button)findViewById(R.id.playBtn);
                        playBtnCode.setEnabled(false);
                    }
                });
            }
        }, time, time);

    }

    public void stop(View view) {
        T.cancel();
        Button playBtnCode = (Button)findViewById(R.id.playBtn);
        playBtnCode.setEnabled(true);
        Button stopBtnCode = (Button)findViewById(R.id.stopBtn);
        stopBtnCode.setEnabled(false);
    }
}