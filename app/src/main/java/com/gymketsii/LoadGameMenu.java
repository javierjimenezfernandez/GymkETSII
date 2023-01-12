package com.gymketsii;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

public class LoadGameMenu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_game_menu);
        showPlayerList();
    }

    public void showPlayerList() {
        SQLiteManager admin = new SQLiteManager(getApplicationContext(), "administration", null, 1);
        SQLiteDatabase db = admin.getReadableDatabase();
        TextView player_list = (TextView) findViewById(R.id.textView_player_list);
        //Cursor row = db.rawQuery("select player_name,current_level from player_list", null);
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
}