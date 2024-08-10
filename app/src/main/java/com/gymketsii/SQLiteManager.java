/**
 * GYMKETSII App
 * @file SQLiteManager.java
 *
 * GymkETSII dev team:
 * @author 19235 - Javier Martínez Ciria <javier.martinez.ciria@alumnos.upm.es>
 * @author 11210 - Javier Jiménez Fernández <j.jfernandez@alumnos.upm.es>
 */

package com.gymketsii;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteManager extends SQLiteOpenHelper {

    // Connection between SQLiteManager and GymkETSII App
    public SQLiteManager(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate (SQLiteDatabase db) {
        // Dataset creation:
        // Table to store all players data: "player_list"
        db.execSQL("create table player_list(player_name text primary key, current_level integer)");
        // Auxiliary dataset to store current player data during the game: "current_player_data"
        db.execSQL("create table current_player_data(player_name text primary key, current_level integer)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // "player_list"
        db.execSQL("drop table if exists player_list");
        db.execSQL("create table player_list(player_name text primary key, current_level integer)");
        // "current_player_data"
        db.execSQL("drop table if exists current_player_data");
        db.execSQL("create table current_player_data(player_name text primary key, current_level integer)");
    }
}
