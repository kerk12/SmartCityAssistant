package com.kerk12.smartcityassistant;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.List;

public class SmartHome extends AppCompatActivity {

    private RecyclerView SmartHomeRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager SPMan;

    private TextView deviceName, deviceLocation, extra_settings_label, deviceStatus;
    private ToggleButton powerButton;
    private int deviceSelected = 0;
    private LinearLayout errors_layout, extraSettingsLayout;
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
            SmartDevice sdev = SmartDeviceManager.getDeviceList(getApplicationContext()).get(position);
            switch (sdev.getCategory()){
                case LIGHTING:
                    holder.category.setImageResource(R.drawable.lighting_category);
                    break;
                case ENTERTAINMENT:
                    holder.category.setImageResource(R.drawable.entertainment_category);
                    break;
                case GENERIC:
                    holder.category.setImageResource(R.drawable.generic_category);
                    break;
                default:
                    holder.category.setVisibility(View.GONE);
            }
            if (sdev.isActivated()){
                holder.deviceName.setTextColor(getResources().getColor(R.color.device_enabled));
            } else {
                holder.deviceName.setTextColor(getResources().getColor(R.color.device_disabled));
            }
            holder.deviceName.setText(mList.get(position).getName());
            holder.deviceLocation.setText(mList.get(position).getLocation());
            holder.list_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deviceSelected = position;
                    UpdateDetails();
                }
            });
            if (sdev.hasError()){
                holder.error_icon.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public int getItemCount() {
            return mList.size();
        }

        protected class ViewHolder extends RecyclerView.ViewHolder{

            public TextView deviceName, deviceLocation;
            public RelativeLayout list_item;
            public ImageView error_icon, category;
            public ViewHolder(View itemView) {
                super(itemView);
                category = (ImageView) itemView.findViewById(R.id.device_category);
                deviceName = (TextView) itemView.findViewById(R.id.device_name_on_list);
                deviceLocation = (TextView) itemView.findViewById(R.id.device_location_on_list);
                list_item = (RelativeLayout) itemView.findViewById(R.id.device_list_item);
                error_icon = (ImageView) itemView.findViewById(R.id.error_image);
                error_icon.setImageResource(R.drawable.error);
            }
        }
    }
    public void UpdateAdapter(){
        mAdapter = null;
        mAdapter = new SHAdapter(SmartDeviceManager.getDeviceList(getApplicationContext()));
        SmartHomeRecyclerView.setAdapter(mAdapter);
    }


    private void UpdateDetails(){
        SmartDevice smartDev = SmartDeviceManager.getDeviceList(getApplicationContext()).get(deviceSelected);
        deviceName.setText(smartDev.getName());
        deviceLocation.setText(smartDev.getLocation());
        if (smartDev.isActivated()){
            deviceStatus.setTextColor(getResources().getColor(R.color.device_enabled));
            deviceStatus.setText("Ενεργοποιμένη");
        } else {
            deviceStatus.setTextColor(getResources().getColor(R.color.device_disabled));
            deviceStatus.setText("Απενεργοποιημένη");
        }
        powerButton.setChecked(smartDev.isActivated());

        extraSettingsLayout.removeAllViews();
        List<DeviceExtraSetting> extraSettings = smartDev.getExtraSettings();

        if (extraSettings.size() > 0){
            extra_settings_label.setVisibility(View.VISIBLE);
            for (DeviceExtraSetting s: extraSettings){
                List<View> views = s.getFinalOutput(getApplicationContext());
                for (View v: views){
                    extraSettingsLayout.addView(v);
                }
            }
        } else {
            extra_settings_label.setVisibility(View.INVISIBLE);
        }
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
        getSupportActionBar().setTitle(R.string.SmartHomeManager);
        SmartHomeRecyclerView = (RecyclerView) findViewById(R.id.smart_recycler);
        SPMan = new LinearLayoutManager(this);
        SmartHomeRecyclerView.setLayoutManager(SPMan);
        mAdapter = new SHAdapter(SmartDeviceManager.getDeviceList(getApplicationContext()));
        SmartHomeRecyclerView.setAdapter(mAdapter);

        deviceName = (TextView) findViewById(R.id.device_name);
        deviceLocation = (TextView) findViewById(R.id.device_location);

        deviceStatus = (TextView)findViewById(R.id.device_status);

        errors_layout = (LinearLayout) findViewById(R.id.errors_layout);
        errors = (TextView) findViewById(R.id.device_error);
        extra_settings_label = (TextView) findViewById(R.id.extra_settings_label);
        extraSettingsLayout = (LinearLayout) findViewById(R.id.extra_settings);

        powerButton = (ToggleButton) findViewById(R.id.power_button);

        powerButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SmartDeviceManager.SetPower(deviceSelected, isChecked);
                UpdateAdapter();
                UpdateDetails();
            }
        });

        UpdateDetails();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.copied_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.help_menu_choice:
                if (!MainMenuFragment.checkNetworkConnectivity(getApplicationContext())){
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.internet_not_connected), Toast.LENGTH_LONG).show();
                    return false;
                }
                Intent i = new Intent(getApplicationContext(), HelpActivity.class);
                startActivity(i);
        }
        return true;
    }
}
