/**
 * GYMKETSII App
 * @file WelcomeScreen.java
 *
 * GymkETSII dev team:
 * @author 19235 - Javier Martínez Ciria <javier.martinez.ciria@alumnos.upm.es>
 * @author 11210 - Javier Jiménez Fernández <j.jfernandez@alumnos.upm.es>
 */

package com.gymketsii;

import static android.nfc.NdefRecord.createUri;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.Window;

import java.util.Timer;
import java.util.TimerTask;

public class WelcomeScreen extends AppCompatActivity {

    private static final long DELAY = 3000;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_welcome_screen);



        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                Intent intent = new Intent().setClass(WelcomeScreen.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        };
        Timer crono = new Timer();
        crono.schedule(task, DELAY);
    }
}