package com.gymketsii;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.media.MediaPlayer;
import androidx.appcompat.app.AppCompatActivity;

public class WinScreen extends AppCompatActivity{

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_win_screen);
        }

        public void goToLevel2(View view) {
            Intent intent = new Intent(this, Level2.class);
            startActivity(intent);
        }
}
