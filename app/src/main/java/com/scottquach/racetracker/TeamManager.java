package com.scottquach.racetracker;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ScrollingTabContainerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nhaarman.listviewanimations.appearance.simple.AlphaInAnimationAdapter;
import com.nhaarman.listviewanimations.itemmanipulation.DynamicListView;
import com.nhaarman.listviewanimations.itemmanipulation.swipedismiss.OnDismissCallback;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class TeamManager extends AppCompatActivity {

    private ListView teamListView;

    private ArrayList<String> teamList = new ArrayList<String>();
    private ArrayAdapter<String> myAdapter;

    private String teamName;

    private RelativeLayout backgroundLayout;

    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_manager);

        //initial set up
        sharedPref = getSharedPreferences("Array Storage File", Context.MODE_PRIVATE);
        editor = sharedPref.edit();
        backgroundLayout = (RelativeLayout)findViewById(R.id.teamManagerBackgroundLayout);

        //retrieve teamName
        Bundle extras = getIntent().getExtras();
        if (extras != null){
            teamName = extras.getString("teamName");
        }

        //set ID
        teamListView = (ListView) findViewById(R.id.listView);

        //Setup
        setUpListView();

        teamListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                teamList.remove(position);
                myAdapter.notifyDataSetChanged();
                Snackbar.make(backgroundLayout,"Item Deleted",Snackbar.LENGTH_SHORT).show();

//                for (int i = 0; i < teamsArray.size(); i++){
//                    if (teamsArray.get(i+1) == null){
//                        if (teamsArray.get(i+2) != null){
//                            teamsArray.set(i+1,teamsArray.get(i+2));
//                        }
//                    }
//                }

                Gson gson = new Gson();
                String json = gson.toJson(teamList);
                editor.putString(teamName, json);
                editor.commit();

            }
        });

        Toast.makeText(this,"Add the teams here",Toast.LENGTH_SHORT).show();
    }

    private void setUpListView(){

        //Temp methods
//        namesList.add("Shorewood");
//        namesList.add("Shorecrest");
//        namesList.add("Nathan Hale");

        //Retrieve array of meet
        Gson gson = new Gson();
        String json = sharedPref.getString(teamName, null);
        if (json != null){
            Type type = new TypeToken<ArrayList<String>>() {}.getType();
            teamList = gson.fromJson(json, type);
        }



        myAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,teamList);

//        AlphaInAnimationAdapter animationAdapter = new AlphaInAnimationAdapter(myAdapter);
//        animationAdapter.setAbsListView(teamListView);
//        teamListView.setAdapter(animationAdapter);
        teamListView.setAdapter(myAdapter);



    }

    public void addButtonClicked(View view) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final EditText input = new EditText(this);
        input.setHint("Team/School Name");
        builder.setView(input);
        builder.setTitle("Add Team");
        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Add team
                String inputString = input.getText().toString();
                teamList.add(inputString);

                Gson gson = new Gson();
                String json = gson.toJson(teamList);
                editor.putString(teamName, json);
                editor.commit();


            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();

    }

    public void raceButtonClicked(View view) {
        Intent intent = new Intent(TeamManager.this, ScoreInput.class);
        intent.putExtra("teamName",teamName);
        startActivity(intent);
    }
}
