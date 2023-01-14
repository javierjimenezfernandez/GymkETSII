package com.gymketsii;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class NewGameMenu extends AppCompatActivity {

    private EditText player_name;
    private int starting_level;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_game_menu);

        // Gets the player name from the EditText box in activity_new_game_menu
        player_name = (EditText) findViewById(R.id.editText_insert_name);
        // Sets the starting level to 0 by default
        starting_level = 0;
        Button button_start_game = (Button) findViewById(R.id.button_start_game);
        button_start_game.setOnClickListener(new View.OnClickListener() {
            public void onClick (View v) {
                if(addNewPlayer()){
                    setCurrentPlayerData();
                    startGame();
                }
            }
        });
    }

    /** boolean addNewPlayer
     *
     * Add new register to the database.
     *
     * If the name is already in the player list
     * it will show a warning message. If not, a
     * correct registration message will be shown.
     */
    private boolean addNewPlayer() {
        SQLiteManager admin = new SQLiteManager(getApplicationContext(), "administration", null, 1);
        SQLiteDatabase db = admin.getWritableDatabase();
        String name_to_add = player_name.getText().toString();
        int level = starting_level;
        if (playerQuery(name_to_add)){
            Toast.makeText(getApplicationContext(), "Player name already exists, please choose another one", Toast.LENGTH_SHORT).show();
            return false;
        }
        else {
            ContentValues new_player_values = new ContentValues();
            new_player_values.put("player_name", name_to_add);
            new_player_values.put("current_level", level);
            db.insert("player_list", null, new_player_values);
            db.close();
            Toast.makeText(getApplicationContext(), "Player registration OK", Toast.LENGTH_SHORT).show();
            return true;
        }
    }

    // Checks if a player is already in the player list
    protected boolean playerQuery(String name) {
        SQLiteManager admin = new SQLiteManager(getApplicationContext(), "administration", null, 1);
        SQLiteDatabase db = admin.getReadableDatabase();
        String name_to_check = name;
        String selection = "player_name = ?";
        String[] selectionArgs = {name_to_check};
        Cursor row = db.query(
                "player_list",             // Table to query
                new String[]{"player_name"},    // The array of columns to return (pass null to get all)
                selection,                      // The columns for the WHERE clause
                selectionArgs,                  // The values for the WHERE clause
                null,                   // don't group the rows
                null,                    // don't filter by row groups
                null                    // The sort order
        );
        while (row.moveToNext()) {
            if (name_to_check.equals(row.getString(0))) {
                return true;
            }
        }
        return false;
    }

    // Overrides auxiliary dataset with current player data, then updates player list with current player data
    protected void setCurrentPlayerData (){
        // First delete old player data in auxiliary table
        resetCurrentPlayerData();
        SQLiteManager admin = new SQLiteManager(getApplicationContext(), "administration", null, 1);
        SQLiteDatabase db = admin.getWritableDatabase();
        String name = player_name.getText().toString();
        // First level is level 1
        int level = 1;
        ContentValues new_current_player = new ContentValues();
        new_current_player.put("player_name", name);
        new_current_player.put("current_level", level);
        // Sets auxiliary table with current player data
        db.insert("current_player_data", null, new_current_player);
        db.close();
        Toast.makeText(getApplicationContext(), "Set Current Player Data OK", Toast.LENGTH_SHORT).show();
        // Updates player list table with updated player data
        updatePlayerListDatabase(name, level);
    }

    // Reset auxiliary dataset containing possible Current Player data
    protected void resetCurrentPlayerData () {
        SQLiteManager admin = new SQLiteManager(getApplicationContext(), "administration", null, 1);
        SQLiteDatabase db = admin.getWritableDatabase();

        if (checkCurrentPlayerDataExistence() == 1) {
            int delete_result = db.delete("current_player_data", null, null);
            db.close();
            if (delete_result == 1) {
                Toast.makeText(getApplicationContext(), "Reset Current Player Data OK", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(getApplicationContext(), "Delete Current Player Data Error", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Checks if there is already Current Player data in the auxiliary dataset
    protected int checkCurrentPlayerDataExistence() {
        SQLiteManager admin = new SQLiteManager(getApplicationContext(), "administration", null, 1);
        SQLiteDatabase db = admin.getReadableDatabase();
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
            Toast.makeText(getApplicationContext(), "No current player yet", Toast.LENGTH_SHORT).show();
        }
        else if (row_amount==1) {
            result = 1;
            Toast.makeText(getApplicationContext(), "Only one existing current player, everything OK", Toast.LENGTH_SHORT).show();
        }
        else {
            result = 1;
            Toast.makeText(getApplicationContext(), "Something went wrong, more than one current player in the list", Toast.LENGTH_SHORT).show();
        }
        return result;
    }

    // Updates player list table with new player data
    protected void updatePlayerListDatabase(String name, int level) {
        SQLiteManager admin = new SQLiteManager(getApplicationContext(), "administration", null, 1);
        SQLiteDatabase db = admin.getWritableDatabase();
        ContentValues current_player_data = new ContentValues();
        current_player_data.put("player_name", name);
        current_player_data.put("current_level", level);
        int result = db.update("player_list", current_player_data, "player_name=?", new String[]{name});
        db.close();
        if (result == 1){
            Toast.makeText(getApplicationContext(), "Update Player List database with Current Player Data OK", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(getApplicationContext(), "Update Player List database with Current Player Data ERROR. No player with the name:" + name, Toast.LENGTH_SHORT).show();
        }
    }

    public void startGame () {
        Intent intent = new Intent(this, Level1.class);
        startActivity(intent);
    }

}