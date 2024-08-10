/**
 * GYMKETSII App
 * @file Level1.java
 *
 * GymkETSII dev team:
 * @author 19235 - Javier Martínez Ciria <javier.martinez.ciria@alumnos.upm.es>
 * @author 11210 - Javier Jiménez Fernández <j.jfernandez@alumnos.upm.es>
 */

package com.gymketsii;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class Level1 extends AppCompatActivity {

    private EditText Resultado;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level1);
    }

    public void CompruebaLevel1 (View view) {
        Resultado = (EditText) findViewById(R.id.editTextNumberDecimal_level1);
        String ResultadoString = Resultado.getText().toString();
        double ResultadoDouble = Double.parseDouble(ResultadoString);
        if( ResultadoDouble == 3.20){
            Intent intent = new Intent(this, Level1WinScreen.class);
            startActivity(intent);
        }
        else{
            Toast.makeText(getApplicationContext(),"Error", Toast.LENGTH_SHORT).show();
        }
    }


}