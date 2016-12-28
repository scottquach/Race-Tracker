package com.scottquach.racetracker;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class ScoreInput extends AppCompatActivity {

    private ListView sInputListView;
    private ArrayAdapter<String> myAdapter;
    private ArrayList<String> teamsArray = new ArrayList<String>();
    private ArrayList<String> rankArray = new ArrayList<String>();

    private String teamName;
    private int rankNumber = 1;

    private RelativeLayout inputLayout;

    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score_input);

        //set ID
        sInputListView = (ListView)findViewById(R.id.scoreInputListView);
        inputLayout = (RelativeLayout)findViewById(R.id.inputLayout);
        sharedPref = getSharedPreferences("Array Storage File", Context.MODE_PRIVATE);
        editor = sharedPref.edit();

        //setUp methods
        Bundle extras = getIntent().getExtras();
        if (extras != null){
            teamName = extras.getString("teamName");
        }

        setUpListView();

        sInputListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String team = ((TextView)view).getText().toString();
                rankArray.add(String.valueOf(rankNumber)+ " -    " + team);
                Snackbar.make(inputLayout, "Ranked: " + String.valueOf(rankNumber), Snackbar.LENGTH_SHORT).show();
                rankNumber++;
                teamsArray.remove(position);
                myAdapter.notifyDataSetChanged();


//                for (int i = 0; i < teamsArray.size(); i++){
//                    if (teamsArray.get(i+1) == null){
//                        if (teamsArray.get(i+2) != null){
//                            teamsArray.set(i+1,teamsArray.get(i+2));
//                        }
//                    }
//                }

                Gson gson = new Gson();
                String json = gson.toJson(rankArray);
                editor.putString(teamName + "key", json);
                editor.commit();

            }
        });

        Toast.makeText(this, "Select team as they finish the race", Toast.LENGTH_SHORT).show();

    }

    private void setUpListView(){

        Gson gson = new Gson();
        String json = sharedPref.getString(teamName, null);
        if (json != null){
            Type type = new TypeToken<ArrayList<String>>() {}.getType();
            teamsArray = gson.fromJson(json, type);
        }

        myAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, teamsArray);
        sInputListView.setAdapter(myAdapter);

    }

    /*
    Button Clicks
     */

    public void viewScoreButtonClicked(View view) {
        Intent openScoreTracker = new Intent(ScoreInput.this,ScoreTracker.class);
        openScoreTracker.putExtra("teamName",teamName);
        startActivity(openScoreTracker);
    }
}
