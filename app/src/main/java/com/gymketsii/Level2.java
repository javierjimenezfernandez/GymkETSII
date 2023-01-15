/**
 * GYMKETSII App
 * @file Level2.java
 *
 * GymkETSII dev team:
 * @author 19235 - Javier Martínez Ciria <javier.martinez.ciria@alumnos.upm.es>
 * @author 11210 - Javier Jiménez Fernández <j.jfernandez@alumnos.upm.es>
 */

package com.gymketsii;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class Level2 extends AppCompatActivity implements SensorEventListener {

    private SensorManager gestor;
    private Sensor sensor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level2);

        gestor = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensor = gestor.getDefaultSensor(Sensor.TYPE_LIGHT);
    }
    @Override
    public final void onAccuracyChanged(Sensor sen, int acc){

    }
    @Override
    public final void onSensorChanged(SensorEvent event){
        float luzMedida = event.values[0];
        if(luzMedida<10){
            Intent intent = new Intent(this, Level2WinScreen.class);
            startActivity(intent);
        }
    }
    @Override
    protected void onResume(){
        super.onResume();
        gestor.registerListener(this,sensor,SensorManager.SENSOR_DELAY_NORMAL);
    }
    @Override
    protected void onPause(){
        super.onPause();
        gestor.unregisterListener(this,sensor);
    }


    public void goToWinScreen2(View view) {
        Intent intent = new Intent(this, Level2WinScreen.class);
        startActivity(intent);
    }
}