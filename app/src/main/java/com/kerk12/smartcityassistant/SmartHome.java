package com.kerk12.smartcityassistant;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

public class SmartHome extends AppCompatActivity {

    private RecyclerView SmartHomeRecyclerView;

    private class SHAdapter extends RecyclerView.Adapter<SHAdapter.SmartHomeViewHolder>{
        @Override
        public SmartHomeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return null;
        }

        @Override
        public void onBindViewHolder(SmartHomeViewHolder holder, int position) {

        }

        @Override
        public int getItemCount() {
            return 0;
        }

        protected class SmartHomeViewHolder extends RecyclerView.ViewHolder{

            public SmartHomeViewHolder(View itemView) {
                super(itemView);
            }
        }
    }


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smart_home);

        SmartHomeRecyclerView = (RecyclerView) findViewById(R.id.smart_recycler);

    }
}
