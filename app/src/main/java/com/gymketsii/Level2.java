package com.gymketsii;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Level2 extends AppCompatActivity {

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
            Intent intent = new Intent(this, WinScreen.class);
            startActivity(intent);
        }
        else{
            Toast.makeText(getApplicationContext(),"Error", Toast.LENGTH_SHORT).show();
        }
    }

    public void goToWinScreen(View view) {
        Intent intent = new Intent(this, WinScreen.class);
        startActivity(intent);
    }
}