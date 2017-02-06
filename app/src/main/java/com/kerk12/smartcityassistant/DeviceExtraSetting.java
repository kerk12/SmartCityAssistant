package com.kerk12.smartcityassistant;

import android.app.ActionBar;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.android.gms.vision.text.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kgiannakis on 6/2/2017.
 */

public class DeviceExtraSetting{
    public static final String SLIDER = "slider";

    private static final int STYLE = R.style.ExtraSetting;
    int currentVal = -1;
    List<View> finalOutput = new ArrayList<View>();
    public DeviceExtraSetting(String name, String type, Context context){
        switch (type){
            case SLIDER:
                TextView label = new TextView(context);
                label.setTextColor(context.getResources().getColor(android.R.color.primary_text_light));
                label.setText(name);
                LinearLayout seekerLayout = new LinearLayout(context);
                seekerLayout.setOrientation(LinearLayout.HORIZONTAL);
                seekerLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

                SeekBar seeker = new SeekBar(context);
                seeker.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 0.9f));
                final TextView currentValue = new TextView(context);
                currentValue.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 0.1f));
                seeker.setMax(10);
                if (currentVal != -1) {
                    currentValue.setText(String.valueOf(currentVal) + "/" + String.valueOf(10));
                    seeker.setProgress(currentVal);
                } else {
                    currentValue.setText(String.valueOf(5) + "/" + String.valueOf(10));
                    seeker.setProgress(5);
                }

                seeker.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        currentValue.setText(String.valueOf(progress)+"/"+String.valueOf(seekBar.getMax()));
                        currentVal = progress;
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                });
                finalOutput.add(label);
                seekerLayout.addView(seeker);
                seekerLayout.addView(currentValue);
                finalOutput.add(seekerLayout);
                break;
            default:
                throw new IllegalArgumentException();
        }
    }

    public List<View> getFinalOutput(){
        return finalOutput;
    }
}