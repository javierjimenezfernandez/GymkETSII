package com.gymketsii;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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
                    startGame();
                }
            }
        });
    }

    /** boolean add_new_player
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
            player_name.setText("");
            Toast.makeText(getApplicationContext(), "Player registration OK", Toast.LENGTH_SHORT).show();
            return true;
        }
    }

    // Checks if a player is already in the player list
    public boolean playerQuery(String name) {
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

    public void startGame () {
        Intent intent = new Intent(this, Level1.class);
        startActivity(intent);
    }

}