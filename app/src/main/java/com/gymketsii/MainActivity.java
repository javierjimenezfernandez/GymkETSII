/**
 * GYMKETSII App
 * @file MainActivity.java
 *
 * GymkETSII dev team:
 * @author 19235 - Javier Martínez Ciria <javier.martinez.ciria@alumnos.upm.es>
 * @author 11210 - Javier Jiménez Fernández <j.jfernandez@alumnos.upm.es>
 *
 *     credits: Alexander Nakarada for the song "Adventure"
 *     (Royalty Free Music)
 */

package com.gymketsii;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

public class MainActivity extends AppCompatActivity {
    public MediaPlayer cancion;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        cancion = MediaPlayer.create(this, R.raw.musicafondo);

            if(cancion.isPlaying()==false) {
                cancion.setLooping(true);
                cancion.start();
            }
    }

    public void goToNewGameMenu (View view) {
        Intent intent = new Intent(this, NewGameMenu.class);
        startActivity(intent);
    }

    public void goToLoadGameMenu (View view) {
        Intent intent = new Intent(this, LoadGameMenu.class);
        startActivity(intent);
    }

    public void goToInstructionsMenu (View view) {
        Intent intent = new Intent(this, InstructionsMenu.class);
        startActivity(intent);
    }
}