package com.onlie.voting.onlinevotingsystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.onlie.voting.onlinevotingsystem.Adapter.PartyVoteAdapter;
import com.onlie.voting.onlinevotingsystem.Class.PartyInfo;
import com.onlie.voting.onlinevotingsystem.Class.VoteTime;
import com.onlie.voting.onlinevotingsystem.Interface.CallBackVote;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class SelectParty extends AppCompatActivity {

    private ProgressDialog LoadingBar;
    String Phone;
    ////////////////////////////////////////////
    private RecyclerView rv_selectParty;
    private PartyVoteAdapter adapter;
    private RecyclerView.LayoutManager mLayoutManager;
    ArrayList<PartyInfo> arrayList;
    FirebaseAuth auth;
    FirebaseUser firebaseUser;
    DatabaseReference reference,refUser,refTime,refVote;
    private CallBackVote callBackVote;

    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm", Locale.getDefault());

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_party);
        init();
        final Intent i = getIntent();
        Phone = i.getStringExtra("phone");
        LoadingBar = new ProgressDialog(this);
        refUser = FirebaseDatabase.getInstance().getReference("Users").child(Phone);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                        PartyInfo p = dataSnapshot1.getValue(PartyInfo.class);
                        arrayList.add(p);
                        rv_selectParty.setAdapter(adapter);
                    }
                }else {
                    PartyInfo p = new PartyInfo("no data",0);
                    arrayList.add(p);
                    rv_selectParty.setAdapter(adapter);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        adapter.setCallback(new CallBackVote() {
            @Override
            public void getVote(final String partyname) {

                        LoadingBar.setTitle("Voting Inprogress");
                        LoadingBar.setMessage("Please wait..");
                        LoadingBar.setCanceledOnTouchOutside(false);
                        LoadingBar.show();

                        refUser.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                final String vote = snapshot.child("Vote").getValue().toString();
                                if (!vote.equals("1")) {
                                    refTime.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            if (snapshot.exists()) {
                                                Date strtDate, endDte;
                                                String startDate = snapshot.child("start").getValue().toString();
                                                String endDate = snapshot.child("end").getValue().toString();
                                                String status = snapshot.child("status").getValue().toString();
                                                if (status.equals("true")) {
                                                    try {
                                                        strtDate = sdf.parse(startDate);
                                                        endDte = sdf.parse(endDate);
                                                        if (new Date().after(strtDate) && new Date().before(endDte)) {
                                                            toast("Voting successful");
                                                            refUser.child("Vote").setValue("1");
                                                            refVote.addListenerForSingleValueEvent(new ValueEventListener() {
                                                                @Override
                                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                    if (snapshot.exists()) {
                                                                        int totalVote = Integer.parseInt(snapshot.child(partyname).getValue().toString());
                                                                        totalVote++;
                                                                        refVote.child(partyname).setValue(totalVote);

                                                                        Intent i = new Intent(SelectParty.this, FinalActivity.class);
                                                                        i.putExtra("phone", Phone);
                                                                        i.putExtra("partyname", partyname);

                                                                        startActivity(i);

                                                                        LoadingBar.dismiss();
                                                                        Toast.makeText(SelectParty.this, "Your Vote is Submitted to our database..", Toast.LENGTH_LONG).show();
                                                                    }
                                                                }

                                                                @Override
                                                                public void onCancelled(@NonNull DatabaseError error) {

                                                                }
                                                            });
                                                        } else {
                                                            toast("Outside the voting period");
                                                            LoadingBar.dismiss();
                                                        }
                                                    } catch (ParseException e) {
                                                        e.printStackTrace();
                                                    }
                                                }else {
                                                    toast("Voting is closed");
                                                    LoadingBar.dismiss();
                                                }
                                            }
                                        }
                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {
                                        }
                                    });
                                }else{
                                    toast("You used your right to vote ... ");
                                    LoadingBar.dismiss();
                                }

                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                            }
                        });
            }
        });
    }
    public void init(){
        rv_selectParty = findViewById(R.id.rv_parties);
        auth = FirebaseAuth.getInstance();
        firebaseUser = auth.getCurrentUser();
        arrayList = new ArrayList<>();
        rv_selectParty.setLayoutManager(new LinearLayoutManager(this));
        reference = FirebaseDatabase.getInstance().getReference("Parties");
        refTime = FirebaseDatabase.getInstance().getReference("VoteTime");
        refVote = FirebaseDatabase.getInstance().getReference("TotalVotes");
        adapter = new PartyVoteAdapter(arrayList,callBackVote);
    }

    public void toast(String string){
        Toast.makeText(getApplicationContext(),string,Toast.LENGTH_SHORT).show();
    }
}
