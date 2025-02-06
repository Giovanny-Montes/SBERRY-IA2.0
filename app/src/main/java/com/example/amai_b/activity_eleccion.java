package com.example.amai_b;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class activity_eleccion extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eleccion);

    }
    public void eleccion_galleria(View view){
        Intent entrar = new Intent(activity_eleccion.this,activity_galleria.class);
        startActivity(entrar);
    }
    public void eleccion_camara(View view){
        Intent entrar = new Intent(activity_eleccion.this,activity_camara.class);
        startActivity(entrar);
    }
}