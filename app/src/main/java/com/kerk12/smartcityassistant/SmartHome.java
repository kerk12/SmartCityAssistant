package com.kerk12.smartcityassistant;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class SmartHome extends AppCompatActivity {

    private RecyclerView SmartHomeRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager SPMan;
    private class SHAdapter extends RecyclerView.Adapter<SHAdapter.ViewHolder>{

        private List<SmartDevice> mList;

        public SHAdapter(List<SmartDevice> mList){
            this.mList = mList;
        }

        public void UpdateAdapter(){
            mAdapter = null;
            mAdapter = new SHAdapter(SmartDeviceManager.getDeviceList());
            SmartHomeRecyclerView.setAdapter(mAdapter);
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.sh_recycler_item, parent,false);

            ViewHolder vh = new ViewHolder(v);
            return vh;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.deviceName.setText(mList.get(position).getName());
        }

        @Override
        public int getItemCount() {
            return mList.size();
        }

        protected class ViewHolder extends RecyclerView.ViewHolder{

            public TextView deviceName;
            public ViewHolder(View itemView) {
                super(itemView);
                deviceName = (TextView) itemView.findViewById(R.id.device_name_on_list);
            }
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

    }
}
