package com.onlie.voting.onlinevotingsystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.onlie.voting.onlinevotingsystem.Class.PartyGraph;
import com.onlie.voting.onlinevotingsystem.Class.PartyVotes;

import java.lang.reflect.Array;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class FinalActivity extends AppCompatActivity {

    TextView V1,V2;
    String PartyName;
    private Button btn_showResults;
    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault());
    DatabaseReference ref,refVote;
    Date endDate;
    private BarChart barChart;
    private ArrayList<BarEntry>  barEntryArrayList;
    private ArrayList<String> labelsNames;
    private ArrayList<PartyGraph> graphList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final);

        init();
        setButton();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {

            case R.id.logout:

                Intent intent=new Intent(FinalActivity.this,welcomeActivity.class);
                startActivity(intent);
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {

    }

    public void init(){
        ref = FirebaseDatabase.getInstance().getReference("VoteTime");
        refVote = FirebaseDatabase.getInstance().getReference("TotalVotes");
        Intent i=getIntent();
        PartyName=i.getStringExtra("partyname");

        V1=(TextView)findViewById(R.id.v1);
        V2=(TextView)findViewById(R.id.v2);

        V2.setText("Your vote is submitted to "+PartyName);

        btn_showResults = findViewById(R.id.btn_showResults);
        barChart = findViewById(R.id.barChart);
    }

    public void setButton(){
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    String status = snapshot.child("status").getValue().toString();
                    String endTime = snapshot.child("end").getValue().toString();
                    try {
                        endDate = sdf.parse(endTime);
                        if (new Date().after(endDate) || status.equals("false")){
                            btn_showResults.setVisibility(View.VISIBLE);
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    public void btn_showResults(View view) {

        showGraph();
    }

    public void showGraph(){

        barEntryArrayList = new ArrayList<>();
        labelsNames = new ArrayList<>();
        refVote.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){

                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                        graphList.add(new PartyGraph(dataSnapshot.getKey(),Integer.parseInt(dataSnapshot.getValue().toString())));
                    }

                    for (int i = 0; i < graphList.size(); i ++){
                        String parties = graphList.get(i).getPartyName();
                        int votes = graphList.get(i).getVote();
                        barEntryArrayList.add(new BarEntry(i,votes));
                        labelsNames.add(parties);
                    }

                    BarDataSet barDataSet = new BarDataSet(barEntryArrayList,"Total Vote");
                    barDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                    Description description = new Description();
                    description.setText("Parties");
                    barChart.setDescription(description);
                    BarData barData = new BarData(barDataSet);
                    barChart.setData(barData);

                    XAxis xAxis = barChart.getXAxis();
                    xAxis.setValueFormatter(new IndexAxisValueFormatter(labelsNames));

                    xAxis.setPosition(XAxis.XAxisPosition.TOP);
                    xAxis.setDrawGridLines(false);
                    xAxis.setDrawAxisLine(false);
                    xAxis.setGranularity(1f);
                    xAxis.setLabelCount(labelsNames.size());
                    xAxis.setLabelRotationAngle(270);
                    barChart.animateY(2000);
                    barChart.invalidate();

                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

    }
}