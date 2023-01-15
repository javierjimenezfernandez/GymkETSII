package com.gymketsii;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoadGameMenuLowerFragment extends Fragment {

    private EditText playerName;

    public LoadGameMenuLowerFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_load_game_menu_lower, container, false);
        // Set player name to be introduced by the user in "EditText_choose_player"
        playerName = (EditText) view.findViewById(R.id.EditText_choose_player);
        // Add on click listener for the Load button
        Button buttonLoad = (Button) view.findViewById(R.id.button_load);
        buttonLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get player name introduced by the user
                String name_to_load = playerName.getText().toString();
                // Check whether player name exists in the player list
                if (playerQuery(name_to_load)) {
                    // If it does, load the saved game
                    loadGame(name_to_load);
                }
                else {
                    Toast.makeText(getContext(), "Player: " + name_to_load + "\n" + "Load Game incorrect, please check spelling", Toast.LENGTH_SHORT).show();
                }
            }
        });
        // Add on click listener for the Delete button
        Button buttonDelete = (Button) view.findViewById(R.id.button_delete);
        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get player name introduced by the user
                String name_to_delete = playerName.getText().toString();
                // Check whether player name exists in the player list
                if (playerQuery(name_to_delete)) {
                    // If it does, delete the saved game and update de table
                    deletePlayer(name_to_delete);
                    showPlayerList(view);
                }
                else {
                    Toast.makeText(getContext(), "Player: " + name_to_delete + "\n" + "Delete Game incorrect, please check spelling", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }

    private void loadGame(String name) {

        int level = levelQuery(name);
        setCurrentPlayerData(name, level);
        Toast.makeText(getContext(), "Player: " + name + " - Current Level: " + level + "\n" + "Load Game successful", Toast.LENGTH_SHORT).show();
        goToLevel(level);

    }

    private void deletePlayer(String name){

        SQLiteManager admin = new SQLiteManager(getContext(), "administration", null, 1);
        SQLiteDatabase db = admin.getReadableDatabase();
        String name_to_delete = name;
        // Delete selected player
        int delete_result = db.delete("player_list",  "player_name=?", new String[]{name});
        db.close();
        // Set the EditText box to empty
        playerName.setText("");
        if (delete_result == 1){
            Toast.makeText(getContext(), "Player: " + name_to_delete + "\n" + "Delete saved game successful", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(getContext(), "Player: " + name_to_delete + "\n" + "Delete saved game ERROR", Toast.LENGTH_SHORT).show();
        }
    }

    // Checks if a player is already in the Player List database
    protected boolean playerQuery (String name) {
        String name_to_check = name;
        Cursor row = generalQuery(name_to_check);
        while (row.moveToNext()) {
            if (name_to_check.equals(row.getString(0))) {
                return true;
            }
        }
        return false;
    }

    // Gets the current level of a player from the Player List database
    protected int levelQuery (String name) {
        String name_to_check = name;
        Cursor row = generalQuery(name_to_check);
        while (row.moveToNext()) {
            if (name_to_check.equals(row.getString(0))) {
                return row.getInt(1);
            }
        }
        return 0;
    }

    // General query that returns a Cursor to the Player Name matching "name"
    // and its Current Level from the Player List database.
    protected Cursor generalQuery (String name) {
        SQLiteManager admin = new SQLiteManager(getContext(), "administration", null, 1);
        SQLiteDatabase db = admin.getReadableDatabase();
        String name_to_check = name;
        String selection = "player_name = ?";
        String[] selectionArgs = {name_to_check};
        Cursor row = db.query(
                "player_list",             // Table to query
                null,                   // The array of columns to return (pass null to get all)
                selection,                      // The columns for the WHERE clause
                selectionArgs,                  // The values for the WHERE clause
                null,                   // don't group the rows
                null,                    // don't filter by row groups
                null                    // The sort order
        );

        return row;
    }

    // Overrides auxiliary dataset with current player data, then updates player list with current player data
    protected void setCurrentPlayerData (String name, int level){
        // First delete old player data in auxiliary table
        resetCurrentPlayerData(name);
        SQLiteManager admin = new SQLiteManager(getContext(), "administration", null, 1);
        SQLiteDatabase db = admin.getWritableDatabase();
        // First level is level 1
        ContentValues new_current_player = new ContentValues();
        new_current_player.put("player_name", name);
        new_current_player.put("current_level", level);
        // Sets auxiliary table with current player data
        db.insert("current_player_data", null, new_current_player);
        db.close();
        /** Toast for test purposes:
        Toast.makeText(getContext(), "Set Current Player Data OK", Toast.LENGTH_SHORT).show();
         */
    }

    // Reset auxiliary dataset containing possible Current Player data
    protected void resetCurrentPlayerData (String name) {
        SQLiteManager admin = new SQLiteManager(getContext(), "administration", null, 1);
        SQLiteDatabase db = admin.getWritableDatabase();

        if (checkCurrentPlayerDataExistence(name) == 1) {
            int delete_result = db.delete("current_player_data", null, null);
            db.close();
            if (delete_result == 1) {
                /** Toast for test purposes:
                Toast.makeText(getContext(), "Reset Current Player Data OK", Toast.LENGTH_SHORT).show();
                 */
            }
            else {
                /** Toast for test purposes:
                Toast.makeText(getContext(), "Delete Current Player Data Error", Toast.LENGTH_SHORT).show();
                 */
            }
        }
    }

    // Checks if there is already Current Player data in the auxiliary dataset
    protected int checkCurrentPlayerDataExistence (String name) {
        SQLiteManager admin = new SQLiteManager(getContext(), "administration", null, 1);
        SQLiteDatabase db = admin.getReadableDatabase();
        // To avoid FATAL EXCEPTION in some devices when trying to read a table that does not exist we create the table
        // only in the case it does not exist yet.
        db.execSQL("create table if not exists current_player_data(player_name text primary key, current_level integer)");
        // Now we can safely read the table
        int result = 0;
        Cursor row = db.query(
                "current_player_data",             // Table to query
                null,                   // The array of columns to return (pass null to get all)
                null,                   // The columns for the WHERE clause
                null,                // The values for the WHERE clause
                null,                   // don't group the rows
                null,                    // don't filter by row groups
                null                    // The sort order
        );
        int row_amount = 0;
        while (row.moveToNext()) {
            row_amount++;
        }
        db.close();
        if (row_amount==0) {
            /** Toast for test purposes:
            Toast.makeText(getContext(), "No current player yet", Toast.LENGTH_SHORT).show();
             */
        }
        else if (row_amount==1) {
            result = 1;
            /** Toast for test purposes:
            Toast.makeText(getContext(), "Only one existing current player, everything OK", Toast.LENGTH_SHORT).show();
             */
        }
        else {
            result = 1;
            /** Toast for test purposes:
            Toast.makeText(getContext(), "Something went wrong, more than one current player in the list", Toast.LENGTH_SHORT).show();
             */
        }
        return result;
    }

    // Updates Saved Games table in the Load Game Menu
    public void showPlayerList(View view) {
        SQLiteManager admin = new SQLiteManager(getContext(), "administration", null, 1);
        SQLiteDatabase db = admin.getReadableDatabase();
        TextView player_list = (TextView) getActivity().findViewById(R.id.textView_player_list);
        Cursor row = db.query(
                "player_list",             // Table to query
                null,                   // The array of columns to return (pass null to get all)
                null,                   // The columns for the WHERE clause
                null,                // The values for the WHERE clause
                null,                   // don't group the rows
                null,                    // don't filter by row groups
                null                    // The sort order
        );
        String complete_list = "";
        int row_amount = 0;
        while (row.moveToNext()) {
            complete_list += "Player name: " + row.getString(0) + " - Current Level: " + row.getString(1) + "\n";
            row_amount++;
        }
        if (row_amount==0) {
            complete_list += "No Games to load";
        }
        player_list.setText(complete_list);
        db.close();
    }

    // Go to a specific level
    // Please note that Level Class name is standardized to Levelx.
    // Where x is an integer
    public void goToLevel(int level) {
        String levelName = "com.gymketsii.Level" + level;
        Intent intent = new Intent();
        intent.setClassName(getActivity(), levelName);
        startActivity(intent);
    }
}