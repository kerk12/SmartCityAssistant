package com.kerk12.smartcityassistant;

import android.app.ActionBar;
import android.content.Context;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
    public static final String NUM_UP_DOWN = "numeric_up_down";

    private String type = null;
    private String name = null;
    int currentVal = -1;

    public DeviceExtraSetting(String name, String type){
        this.name = name;
        this.type = type;
    }

    public List<View> getFinalOutput(Context context){
        //Renew the list
        //NOTE: NEVER reuse views
        List<View> finalOutput = new ArrayList<View>();
        //Generate the label
        TextView label = new TextView(context);
        label.setPadding(0,10,0,0);
        label.setTextColor(context.getResources().getColor(android.R.color.primary_text_light));
        label.setText(name);
        switch (type){
            //If the type is a slider
            case SLIDER:
                /**
                 * Add a linearLayout with:
                 * 1: Seeker
                 * 2: TextView with the current value.
                 */
                LinearLayout seekerLayout = new LinearLayout(context);
                seekerLayout.setOrientation(LinearLayout.HORIZONTAL);
                seekerLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

                //Make the seeker
                SeekBar seeker = new SeekBar(context);
                seeker.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 0.9f));
                //Make the textview
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
            case NUM_UP_DOWN:
                LinearLayout num_layout = new LinearLayout(context);
                Button increment = new Button(context);
                increment.setText("+");

                final TextView currentValue2 = new TextView(context);
                currentValue2.setTextColor(context.getResources().getColor(android.R.color.primary_text_light));
                currentValue2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 22);
                currentValue2.setPadding(10,0,10,0);
                if (currentVal == -1){
                    currentVal = 5;
                }
                currentValue2.setText(String.valueOf(currentVal));

                Button decrement = new Button(context);
                decrement.setText("-");

                increment.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        currentVal++;
                        currentValue2.setText(String.valueOf(currentVal));
                    }
                });

                decrement.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Do not decrement below zero
                        if (currentVal == 0){
                            return;
                        } else {
                            currentVal--;
                            currentValue2.setText(String.valueOf(currentVal));
                        }
                    }
                });
                num_layout.addView(increment);
                num_layout.addView(currentValue2);
                num_layout.addView(decrement);
                num_layout.setOrientation(LinearLayout.HORIZONTAL);
                finalOutput.add(label);
                finalOutput.add(num_layout);
                break;
            default:
                throw new IllegalArgumentException();
        }
        return finalOutput;
    }
}