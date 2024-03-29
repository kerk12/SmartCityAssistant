package com.kerk12.smartcityassistant;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.PolylineOptions;

import java.util.List;

public class ElderMonitoringActivity extends AppCompatActivity {

    private List<Elder> ElderList = ElderManager.getElders();
    private int SelectedElder = -1;
    private boolean SendingMessage = false;

    /**
     * Adapter for managing the elders in the recyclerview.
     */
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
            //When the user clicks on the item
            holder.layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (SendingMessage){
                        Toast.makeText(getApplicationContext(), "Γίνεται αποστολή μηνύματος, παρακαλώ περιμένετε...", Toast.LENGTH_LONG).show();
                        return;
                    }
                    SelectedElder = position;
                    UpdateUI();
                }
            });
            //Name
            holder.name.setText(e.getName());
            //Location
            holder.location.setText(e.getLocation());
            //Condition
            if (e.getCondition() == Elder.Condition.GOOD){
                holder.condition.setText("Καλή κατάσταση");
                holder.condition.setTextColor(getResources().getColor(R.color.device_enabled));
            } else if(e.getCondition() == Elder.Condition.EMERGENCY){
                holder.condition.setText("Έγινε ειδοποίηση των κοινωνικών υπηρεσιών!");
                holder.condition.setTextColor(getResources().getColor(R.color.device_disabled));
            }
            else {
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

    /**
     * For updating the details on the right side.
     */
    private void UpdateUI(){
        Elder e = ElderList.get(SelectedElder);
        name.setText(e.getName());
        location.setText(e.getLocation());
        if (e.getCondition() == Elder.Condition.EMERGENCY){
            emergency_text.setVisibility(View.VISIBLE);
        } else {
            emergency_text.setVisibility(View.GONE);
        }
        cam.setVisibility(View.VISIBLE);
        if (e.getCondition() == Elder.Condition.GOOD){
            cam.setImageResource(R.drawable.old_man);
        } else {
            cam.setImageResource(R.drawable.static_cam);
        }
    }

    /**
     * Update the adapter when something has changed.
     */
    private void UpdateAdapter(){
        ElderList = ElderManager.getElders();
        mAdapter = new ElderAdapter(ElderList);
        elderRecycler.setAdapter(mAdapter);
    }

    /**
     * Used for simulating a status update message to the elder.
     */
    private void SendMsg(){
        Elder e = ElderList.get(SelectedElder);
        AlertDialog.Builder bob = new AlertDialog.Builder(ElderMonitoringActivity.this);
        //sending.setVisibility(View.GONE);
        switch (e.getCondition()){
            case NEEDS_ATTENTION: //The elder needs attention and doesn't respond.
            bob.setMessage("Δεν υπήρξε απάντηση... Έγινε άμεση ειδοποίηση των κοινωνικών υπηρεσιών. ").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    ElderManager.UpdateElderCondition(Elder.Condition.EMERGENCY, SelectedElder);
                    UpdateAdapter();
                    UpdateUI();
                }
            }).show();
            break;
            case GOOD: //The elder is fine.
                bob.setMessage("Μήνυμα από "+ e.getName()+": Καλησπέρα! Καλά είμαι. Σε ευχαριστώ που νοιάζεσαι.").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();
                break;
            case NEEDS_ATTENTION_FINE: //The elder is fine but needs something else.
                bob.setMessage("Μήνυμα από "+ e.getName()+": Καλησπέρα. Καλά είμαι αλλά χρειάζομαι ψώνια για το σπίτι. Θα σε περιμένω να μου φέρεις.").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();
        }
        SendingMessage = false;
    }
    RecyclerView elderRecycler;
    ElderAdapter mAdapter;
    TextView name, location, sendingMsg, emergency_text;
    ImageView cam;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_elder_monitoring);
        getSupportActionBar().setTitle(R.string.ElderMonitoring);
        elderRecycler = (RecyclerView) findViewById(R.id.elder_recycler);
        name = (TextView) findViewById(R.id.elder_name_details) ;
        location = (TextView) findViewById(R.id.elder_location_details) ;
        sendingMsg = (TextView) findViewById(R.id.sending_message);
        mAdapter = new ElderAdapter(ElderList);
        elderRecycler.setAdapter(mAdapter);
        elderRecycler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        chatButton = (Button) findViewById(R.id.send_message_button);
        chatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ElderList.get(SelectedElder).getCondition() == Elder.Condition.EMERGENCY){
                    AlertDialog.Builder bob = new AlertDialog.Builder(ElderMonitoringActivity.this);
                    bob.setMessage("Έχουν ειδοποιηθεί οι κοινωνικές υπηρεσίες. Θα λάβετε τηλεφώνημα, μόλις γίνει ενημέρωση της κατάστασης.").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    }).show();
                    return;
                }
                chatButton.setEnabled(false);
                sendingMsg.setVisibility(View.VISIBLE);
                SendingMessage = true;
                CountDownTimer t = new CountDownTimer(7000,1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {

                    }

                    @Override
                    public void onFinish() {
                        SendMsg();
                        sendingMsg.setVisibility(View.GONE);
                        chatButton.setEnabled(true);
                    }
                }.start();

            }
        });
        emergency_text = (TextView) findViewById(R.id.message_sent);
        cam = (ImageView) findViewById(R.id.camera_elder_mon);
    }

    /*
     *  The Options menu.
     */

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
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
