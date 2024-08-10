/**
 * GYMKETSII App
 * @file Level2WinScreen.java
 *
 * GymkETSII dev team:
 * @author 19235 - Javier Martínez Ciria <javier.martinez.ciria@alumnos.upm.es>
 * @author 11210 - Javier Jiménez Fernández <j.jfernandez@alumnos.upm.es>
 */

package com.gymketsii;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Level2WinScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level2_win_screen);

        // Save current player level
        saveCurrentLevel();

        Button button_next2_game = (Button) findViewById(R.id.button_win_level2);
        button_next2_game.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                goToLevel3();
            }
        });
    }

    // Updates current player auxiliary dataset with current level. If everything goes OK,
    // then it updates player list dataset
    protected void saveCurrentLevel() {
        SQLiteManager admin = new SQLiteManager(getApplicationContext(), "administration", null, 1);
        SQLiteDatabase db = admin.getWritableDatabase();
        // Obtain current player name
        String current_player = getCurrentPlayer();
        // Level 2 completed, next level is level 3
        int level_to_update = 3;
        ContentValues register = new ContentValues();
        register.put("player_name", current_player);
        register.put("current_level", level_to_update);
        // Update current player level in auxiliary table
        int update_result = db.update("current_player_data", register, "player_name=?", new String[]{current_player});
        db.close();
        if (update_result == 1) {
            /** Toast for test purposes:
            Toast.makeText(getApplicationContext(), "Set Current Player Data OK", Toast.LENGTH_SHORT).show();
             */
            // If update was successful, then update player list table with updated player data
            updatePlayerListDatabase(current_player, level_to_update);
        }
        else {
            /** Toast for test purposes:
            Toast.makeText(getApplicationContext(), "Set Current Player Data ERROR", Toast.LENGTH_SHORT).show();
             */
        }

    }

    // Search for the current player in the auxiliary current player dataset
    protected String getCurrentPlayer() {
        SQLiteManager admin = new SQLiteManager(getApplicationContext(), "administration", null, 1);
        SQLiteDatabase db = admin.getReadableDatabase();
        Cursor row = db.query(
                "current_player_data",             // Table to query
                null,                   // The array of columns to return (pass null to get all)
                null,                   // The columns for the WHERE clause
                null,                // The values for the WHERE clause
                null,                   // don't group the rows
                null,                    // don't filter by row groups
                null                    // The sort order
        );
        String current_player = "";
        int row_amount = 0;
        while (row.moveToNext()) {
            current_player += row.getString(0);
            row_amount++;
        }
        db.close();
        // In this case the auxiliary dataset is empty and current_player is and empty string
        if (row_amount==0) {
            /** Toast for test purposes:
            Toast.makeText(getApplicationContext(), "Error: No Games to update", Toast.LENGTH_SHORT).show();
             */
            return current_player;
        }
        // In this case the auxiliary dataset contains only one current_player. Expected normal situation.
        else if (row_amount==1) {
            /** Toast for test purposes:
            Toast.makeText(getApplicationContext(), "Only one current player, everything OK", Toast.LENGTH_SHORT).show();
             */
            return current_player;
        }
        // In this case the auxiliary dataset is corrupt and has multiple current players, to safeguard player list dataset integrity
        // it returns an empty string. Then, overwriting the wrong player data is impossible.
        else {
            /** Toast for test purposes:
            Toast.makeText(getApplicationContext(), "Something went wrong, more than one current player", Toast.LENGTH_SHORT).show();
             */
            current_player = "";
            return current_player;
        }

    }

    // Sets player list table with updated player data
    protected void updatePlayerListDatabase(String name, int level) {
        SQLiteManager admin = new SQLiteManager(getApplicationContext(), "administration", null, 1);
        SQLiteDatabase db = admin.getWritableDatabase();
        ContentValues current_player_data = new ContentValues();
        current_player_data.put("player_name", name);
        current_player_data.put("current_level", level);
        int result = db.update("player_list", current_player_data, "player_name=?", new String[]{name});
        db.close();
        if (result == 1){
            /** Toast for test purposes:
            Toast.makeText(getApplicationContext(), "Update Player List database with Current Player Data OK", Toast.LENGTH_SHORT).show();
             */
        }
        else {
            /** Toast for test purposes:
            Toast.makeText(getApplicationContext(), "Update Player List database with Current Player Data ERROR. No player with the name:" + name, Toast.LENGTH_SHORT).show();
             */
        }
    }

    public void goToLevel3() {
        Intent intent = new Intent(this, Level3.class);
        startActivity(intent);
    }
}
