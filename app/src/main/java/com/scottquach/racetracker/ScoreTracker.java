package com.scottquach.racetracker;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class ScoreTracker extends AppCompatActivity {

    private ListView listView;
    private ArrayAdapter<String> myAdapter;
    private ArrayList<String> teamRankArray = new ArrayList<String>();

    private String teamName;

    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score_tracker);

        //set ID
        listView = (ListView)findViewById(R.id.listView);
        sharedPref = getSharedPreferences("Array Storage File", Context.MODE_PRIVATE);
        editor = sharedPref.edit();

        Bundle extras = getIntent().getExtras();
        if (extras != null){
            teamName = extras.getString("teamName");
        }

        setUpListView();

    }


    private void setUpListView(){
        if(retrieveRankArray() != null){
            teamRankArray = retrieveRankArray();
            myAdapter = new ArrayAdapter<String>(ScoreTracker.this,android.R.layout.select_dialog_item,teamRankArray);
            listView.setAdapter(myAdapter);
        }



    }

    private ArrayList<String> retrieveRankArray(){

        Gson gson = new Gson();
        String json = sharedPref.getString(teamName + "key", null);
        Type type = new TypeToken<ArrayList<String>>() {}.getType();
        ArrayList<String> arrayList = gson.fromJson(json, type);
        return arrayList;
    }

    public void homeButtonClicked(View view) {
        Intent goHome = new Intent(ScoreTracker.this,StartMenu.class);
        startActivity(goHome);
    }
}
