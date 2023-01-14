package com.gymketsii;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class Level3 extends AppCompatActivity implements SensorEventListener {

    private SensorManager Manager;
    private Sensor sensor2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level3);

        Manager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensor2 = Manager.getDefaultSensor(Sensor.TYPE_GRAVITY);
    }
    @Override
    public final void onAccuracyChanged(Sensor sen, int acc){

    }
    @Override
    public final void onSensorChanged(SensorEvent event){
        float giroMedido = event.values[2];
        if(giroMedido>9.5){
            Intent intent = new Intent(this, Level3WinScreen.class);
            startActivity(intent);
        }
    }
    @Override
    protected void onResume(){
        super.onResume();
        Manager.registerListener(this,sensor2,SensorManager.SENSOR_DELAY_NORMAL);
    }
    @Override
    protected void onPause(){
        super.onPause();
        Manager.unregisterListener(this,sensor2);
    }


    public void goToWinScreen3(View view) {
        Intent intent = new Intent(this, Level3WinScreen.class);
        startActivity(intent);
    }
}