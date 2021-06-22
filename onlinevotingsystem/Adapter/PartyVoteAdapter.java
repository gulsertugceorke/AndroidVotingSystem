package com.onlie.voting.onlinevotingsystem.Adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.onlie.voting.onlinevotingsystem.Class.PartyInfo;
import com.onlie.voting.onlinevotingsystem.Interface.CallBackVote;
import com.onlie.voting.onlinevotingsystem.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class PartyVoteAdapter extends RecyclerView.Adapter<PartyVoteAdapter.MyViewHolder> {

    private ArrayList<PartyInfo> infoArrayList;
    FirebaseDatabase firebaseDatabase;
    CallBackVote callBackVote;


    public void setCallback(CallBackVote l){
        callBackVote = l;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        public TextView tv_partyName;
        public CardView cv_party;
        DatabaseReference reference;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_partyName = itemView.findViewById(R.id.tv_partyName);
            cv_party = itemView.findViewById(R.id.cv_party);
        }
    }

    public PartyVoteAdapter(ArrayList<PartyInfo> infoArrayList, CallBackVote callBackVote) {
        this.infoArrayList = infoArrayList;
        this.callBackVote = callBackVote;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_parties,parent,false);
        MyViewHolder myViewHolder = new MyViewHolder(view);

        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {

        PartyInfo partyInfo = infoArrayList.get(position);
        holder.tv_partyName.setText(partyInfo.getPartyname());
        holder.cv_party.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //          20210503_040801
                if (callBackVote != null){
                    callBackVote.getVote(holder.tv_partyName.getText().toString());
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return infoArrayList.size();
    }

}
