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

public class ElderMonitoringActivity extends AppCompatActivity {

    private List<Elder> ElderList = ElderManager.getElders();

    private class ElderAdapter extends RecyclerView.Adapter<ElderAdapter.ViewHolder>{

        private List<Elder> mList;

        public ElderAdapter(List<Elder> mList) {
            this.mList = mList;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.elder_recycler_item, parent, false);

            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            Elder e = mList.get(position);
            holder.name.setText(e.getName());
            holder.location.setText(e.getLocation());
            if (e.getCondition() == Elder.Condition.GOOD){
                holder.condition.setText("Καλή κατάσταση");
                holder.condition.setTextColor(getResources().getColor(R.color.device_enabled));
            } else {
                holder.condition.setText("Απαιτείται προσοχή!");
                holder.condition.setTextColor(getResources().getColor(R.color.device_disabled));
            }
        }


        @Override
        public int getItemCount() {
            return mList.size();
        }

        protected class ViewHolder extends RecyclerView.ViewHolder{

            private TextView name,location,condition;
            public ViewHolder(View itemView) {
                super(itemView);
                name = (TextView) itemView.findViewById(R.id.elder_name);
                location = (TextView) itemView.findViewById(R.id.elder_location);
                condition = (TextView) itemView.findViewById(R.id.elder_condition);
            }
        }
    }

    RecyclerView elderRecycler;
    ElderAdapter mAdapter;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_elder_monitoring);

        elderRecycler = (RecyclerView) findViewById(R.id.elder_recycler);
        mAdapter = new ElderAdapter(ElderList);
        elderRecycler.setAdapter(mAdapter);
        elderRecycler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
    }
}
