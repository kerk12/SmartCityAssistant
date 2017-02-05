package com.kerk12.smartcityassistant;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.List;

public class SmartHome extends AppCompatActivity {

    private RecyclerView SmartHomeRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager SPMan;

    private TextView deviceName, deviceLocation;
    private ToggleButton powerButton;
    private int deviceSelected = 0;
    private LinearLayout errors_layout;
    private TextView errors;


    private class SHAdapter extends RecyclerView.Adapter<SHAdapter.ViewHolder>{

        private List<SmartDevice> mList;

        public SHAdapter(List<SmartDevice> mList){
            this.mList = mList;
        }



        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.sh_recycler_item, parent,false);

            ViewHolder vh = new ViewHolder(v);
            return vh;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, final int position) {
            if (SmartDeviceManager.getDeviceList().get(position).isActivated()){
                holder.deviceName.setTextColor(getResources().getColor(R.color.device_enabled));
            } else {
                holder.deviceName.setTextColor(getResources().getColor(R.color.device_disabled));
            }
            holder.deviceName.setText(mList.get(position).getName());
            holder.list_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deviceSelected = position;
                    UpdateDetails();
                }
            });
            if (SmartDeviceManager.getDeviceList().get(position).hasError()){
                holder.error_icon.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public int getItemCount() {
            return mList.size();
        }

        protected class ViewHolder extends RecyclerView.ViewHolder{

            public TextView deviceName;
            public RelativeLayout list_item;
            public ImageView error_icon;
            public ViewHolder(View itemView) {
                super(itemView);
                deviceName = (TextView) itemView.findViewById(R.id.device_name_on_list);
                list_item = (RelativeLayout) itemView.findViewById(R.id.device_list_item);
                error_icon = (ImageView) itemView.findViewById(R.id.error_image);
                error_icon.setImageResource(R.drawable.error);
            }
        }
    }
    public void UpdateAdapter(){
        mAdapter = null;
        mAdapter = new SHAdapter(SmartDeviceManager.getDeviceList());
        SmartHomeRecyclerView.setAdapter(mAdapter);
    }


    private void UpdateDetails(){
        SmartDevice smartDev = SmartDeviceManager.getDeviceList().get(deviceSelected);
        deviceName.setText(smartDev.getName());
        deviceLocation.setText(smartDev.getLocation());
        powerButton.setChecked(smartDev.isActivated());

        if (smartDev.hasError()){
            powerButton.setEnabled(false);
            errors_layout.setVisibility(View.VISIBLE);
            errors.setText(smartDev.getError());
        } else {
            powerButton.setEnabled(true);
            errors_layout.setVisibility(View.GONE);
            errors.setText("");
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smart_home);

        SmartHomeRecyclerView = (RecyclerView) findViewById(R.id.smart_recycler);
        SPMan = new LinearLayoutManager(this);
        SmartHomeRecyclerView.setLayoutManager(SPMan);
        mAdapter = new SHAdapter(SmartDeviceManager.getDeviceList());
        SmartHomeRecyclerView.setAdapter(mAdapter);

        deviceName = (TextView) findViewById(R.id.device_name);
        deviceLocation = (TextView) findViewById(R.id.device_location);

        errors_layout = (LinearLayout) findViewById(R.id.errors_layout);
        errors = (TextView) findViewById(R.id.device_error);
        powerButton = (ToggleButton) findViewById(R.id.power_button);

        powerButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SmartDeviceManager.SetPower(deviceSelected, isChecked);
                UpdateAdapter();
            }
        });

        UpdateDetails();
    }
}
