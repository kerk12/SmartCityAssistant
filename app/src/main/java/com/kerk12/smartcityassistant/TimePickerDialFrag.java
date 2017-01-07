package com.kerk12.smartcityassistant;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.Context;

import android.os.Bundle;
import android.widget.TimePicker;

import java.util.Calendar;

/**
 * Created by kgiannakis on 7/1/2017.
 */

public class TimePickerDialFrag extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

    public interface TimePickerDialFragListener{
        public void onTimeSelected(int hourOfDay, int minute);
    }

    TimePickerDialFragListener l;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        return new TimePickerDialog(getActivity(),this,hour,minute,true);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        l = (TimePickerDialFragListener) context;
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        l.onTimeSelected(hourOfDay, minute);
    }
}
