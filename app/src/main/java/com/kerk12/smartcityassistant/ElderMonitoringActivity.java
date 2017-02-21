package com.kerk12.smartcityassistant;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

public class ElderMonitoringActivity extends AppCompatActivity {

    private List<Elder> ElderList = ElderManager.getElders();
    private int SelectedElder = -1;

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
        public void onBindViewHolder(ViewHolder holder, final int position) {
            Elder e = mList.get(position);
            holder.layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SelectedElder = position;
                    UpdateUI();
                }
            });
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
            private CardView layout;
            public ViewHolder(View itemView) {
                super(itemView);
                layout = (CardView) itemView.findViewById(R.id.elder_recycler_item_layout);
                name = (TextView) itemView.findViewById(R.id.elder_name);
                location = (TextView) itemView.findViewById(R.id.elder_location);
                condition = (TextView) itemView.findViewById(R.id.elder_condition);
            }
        }
    }

    private Button chatButton;
    private void UpdateUI(){
        Elder e = ElderList.get(SelectedElder);
        name.setText(e.getName());
        location.setText(e.getLocation());
    }

    private void UpdateAdapter(){
        ElderList = ElderManager.getElders();
        mAdapter = new ElderAdapter(ElderList);
        elderRecycler.setAdapter(mAdapter);

    }

    private void SendMsg(){
        Elder e = ElderList.get(SelectedElder);
        AlertDialog.Builder bob = new AlertDialog.Builder(ElderMonitoringActivity.this);
        //sending.setVisibility(View.GONE);
        switch (e.getCondition()){
            case NEEDS_ATTENTION:
            bob.setMessage("Δεν υπήρξε απάντηση... Θα θέλατε να γίνει ειδοποίηση των κοινωνικών υπηρεσιών;")
                    .setPositiveButton("Ναι", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            AlertDialog.Builder bob2 = new AlertDialog.Builder(ElderMonitoringActivity.this);
                            bob2.setMessage("Οι δημόσιες αρχές ειδοποιήθηκαν!").setPositiveButton("ΟΚ", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            }).show();
                        }
                    }).setNegativeButton("Όχι", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            }).show();
            break;
            case GOOD:
                bob.setMessage("Μήνυμα από "+ e.getName()+": Καλησπέρα! Καλά είμαι. Σε ευχαριστώ που νοιάζεσαι.").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();
                break;
            case NEEDS_ATTENTION_FINE:
                bob.setMessage("Μήνυμα από "+ e.getName()+": Καλησπέρα. Καλά είμαι αλλά χρειάζομαι ψώνια για το σπίτι. Θα σε περιμένω να μου φέρεις.").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();
        }
    }
    RecyclerView elderRecycler;
    ElderAdapter mAdapter;
    TextView name, location;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_elder_monitoring);

        elderRecycler = (RecyclerView) findViewById(R.id.elder_recycler);
        name = (TextView) findViewById(R.id.elder_name_details) ;
        location = (TextView) findViewById(R.id.elder_location_details) ;
        mAdapter = new ElderAdapter(ElderList);
        elderRecycler.setAdapter(mAdapter);
        elderRecycler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        chatButton = (Button) findViewById(R.id.send_message_button);
        chatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CountDownTimer t = new CountDownTimer(7000,1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {

                    }

                    @Override
                    public void onFinish() {
                        SendMsg();
                    }
                }.start();

            }
        });
    }
}
