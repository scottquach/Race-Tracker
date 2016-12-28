package com.scottquach.racetracker;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mikepenz.aboutlibraries.Libs;
import com.mikepenz.aboutlibraries.LibsBuilder;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;

public class StartMenu extends AppCompatActivity {


    public ArrayList<String> nameOfTeamsArr = new ArrayList<String>();

    private String selectedTeam;

    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_menu);


            //initial set up
            sharedPref = getSharedPreferences("Array Storage File", Context.MODE_PRIVATE);
            editor = sharedPref.edit();

        //set Custom Font
        TextView titleView = (TextView)findViewById(R.id.textView);
        Typeface customTypeFace = Typeface.createFromAsset(getAssets(),"fonts/FUTRFW.TTF");
        titleView.setTypeface(customTypeFace);


    }

    public void newMeetButtonClicked(View view) {

        AlertDialog.Builder builder = new AlertDialog.Builder(StartMenu.this);
        builder.setTitle("Enter Team Name");
        final EditText input = new EditText(StartMenu.this);
        builder.setView(input);
        builder.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                //store team name
                String inputS = input.getText().toString();

                //load arrays
                Gson gson = new Gson();
                String json = sharedPref.getString(getString(R.string.nameOfTeamsKey), null);
                if (json != null) {
                    Type type = new TypeToken<ArrayList<String>>() {
                    }.getType();
                    nameOfTeamsArr = gson.fromJson(json, type);
                }

                boolean matchingName = false;

                for (int i =0; i<nameOfTeamsArr.size(); i++){
                    if (inputS.equals(nameOfTeamsArr.get(i))){
                        matchingName = true;
                        Toast.makeText(StartMenu.this, "Name Already Exists", Toast.LENGTH_SHORT).show();
                    }
                }

                if (matchingName == false){
                    nameOfTeamsArr.add(inputS);

                    Gson gson2 = new Gson();

                    String json2 = gson2.toJson(nameOfTeamsArr);

                    editor.putString(getString(R.string.nameOfTeamsKey), json2);
                    editor.commit();

                    //go to team manager and send team name
                    Intent openTeamManager = new Intent(StartMenu.this,TeamManager.class);
                    openTeamManager.putExtra("teamName",inputS);
                    startActivity(openTeamManager);
                }






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

    public void loadTeam(View view) {



        //retrieve array of team names
        Gson gson = new Gson();
        String json = sharedPref.getString(getString(R.string.nameOfTeamsKey), null);
        if (json != null){
            Type type = new TypeToken<ArrayList<String>>() {}.getType();
            nameOfTeamsArr = gson.fromJson(json, type);


            AlertDialog.Builder builder = new AlertDialog.Builder(StartMenu.this);
            builder.setTitle("Select Team");
            final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(StartMenu.this, android.R.layout.select_dialog_item, nameOfTeamsArr);

            builder.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String selectedTeam = nameOfTeamsArr.get(which);

                    //go to team manager and send team name
                    Intent openTeamManager = new Intent(StartMenu.this,TeamManager.class);
                    openTeamManager.putExtra("teamName",selectedTeam);
                    startActivity(openTeamManager);
                }
            });

            builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(StartMenu.this);
                    builder.setTitle("Select Meet to Delete");
                    final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(StartMenu.this, android.R.layout.select_dialog_item, nameOfTeamsArr);
                    builder.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //Delete Selected Teams Data

                            String selectedTeam = nameOfTeamsArr.get(which);
                            editor.remove(selectedTeam);
                            editor.remove(selectedTeam + "key");
                            editor.commit();

                            nameOfTeamsArr.remove(selectedTeam);
                            Gson gson = new Gson();

                            String json = gson.toJson(nameOfTeamsArr);

                            editor.putString(getString(R.string.nameOfTeamsKey), json);
                            editor.commit();

                        }
                    });builder.show();
                }
            });

            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.show();



        }else {
            Toast.makeText(StartMenu.this, "No Saves Available", Toast.LENGTH_SHORT).show();
        }






    }

    public void viewScoresButtonClicked(View view) {
        //retrieve array of team names
        Gson gson = new Gson();
        String json = sharedPref.getString(getString(R.string.nameOfTeamsKey), null);
        if (json != null){
            Type type = new TypeToken<ArrayList<String>>() {}.getType();
            nameOfTeamsArr = gson.fromJson(json, type);




            final AlertDialog.Builder builder = new AlertDialog.Builder(StartMenu.this);
            builder.setTitle("Select Team");
            final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(StartMenu.this, android.R.layout.select_dialog_item, nameOfTeamsArr);

            builder.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(final DialogInterface dialog, int which) {
                    selectedTeam = nameOfTeamsArr.get(which);

                    Gson gson = new Gson();
                    String json = sharedPref.getString(selectedTeam + "key", null);
                    Type type = new TypeToken<ArrayList<String>>() {}.getType();
                    ArrayList<String> arrayList = gson.fromJson(json, type);
                    if(arrayList != null){
                        //open listView alertdialog with scores
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(StartMenu.this);
                        builder1.setTitle(selectedTeam + " Scores");
                        final ArrayAdapter<String> arrayAdapter1 = new ArrayAdapter<String>(StartMenu.this, android.R.layout.select_dialog_item,arrayList);
                        builder1.setAdapter(arrayAdapter1, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent openTeamManager = new Intent(StartMenu.this,ScoreTracker.class);
                                openTeamManager.putExtra("teamName",selectedTeam);
                                startActivity(openTeamManager);
                            }
                        });
                        builder1.setPositiveButton("Done", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialog.dismiss();
                            }
                        });
                        builder1.setNegativeButton("Full Page", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent openTeamManager = new Intent(StartMenu.this,ScoreTracker.class);
                                openTeamManager.putExtra("teamName",selectedTeam);
                                startActivity(openTeamManager);
                            }
                        });
                        builder1.show();
                    }else{
                        Toast.makeText(StartMenu.this, "Score Unavailable", Toast.LENGTH_SHORT).show();
                    }





//                    Toast.makeText(StartMenu.this, selectedTeam, Toast.LENGTH_SHORT).show();
//                    //go to team manager and send team name
//                    Intent openTeamManager = new Intent(StartMenu.this,ScoreTracker.class);
//                    openTeamManager.putExtra("teamName",selectedTeam);
//                    startActivity(openTeamManager);
                }
            });

            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.show();



        }else {
            Toast.makeText(StartMenu.this, "No Saves Available", Toast.LENGTH_SHORT).show();
        }
    }

    public void settingsButtonClicked(View view) {
        String[] settingsOptionsArray = {"Reset","About"};

        final AlertDialog.Builder builder = new AlertDialog.Builder(StartMenu.this);
        builder.setTitle("Settings");
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(StartMenu.this,android.R.layout.select_dialog_item,settingsOptionsArray);
        builder.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {


                if (i == 0){
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(StartMenu.this);
                    builder1.setTitle("Are you sure?");
                    builder1.setMessage("This will erease ALL teams and meets");
                    builder1.setPositiveButton("Reset", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            editor.clear();
                            editor.commit();
                            nameOfTeamsArr.clear();
                        }
                    });
                    builder1.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });
                    builder1.show();
                }
                if (i == 1){
                    new LibsBuilder()
                            //provide a style (optional) (LIGHT, DARK, LIGHT_DARK_TOOLBAR)
                            .withActivityStyle(Libs.ActivityStyle.LIGHT)
                            //start the activity
                            .start(StartMenu.this);
                }
            }
        });
        builder.show();

    }
}
