package com.example.sberry_ia20;
/*
# Copyright 2024 Giovanny M.R.

# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at

# http://www.apache.org/licenses/LICENSE-2.0

# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.


 */
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.content.Intent;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void entrar(View view){
        Intent entrar = new Intent(this,activity_eleccion.class);
        startActivity(entrar);
    }

    public void salir(View view){
        finish();
    }
    /* Falta el menú de opciones del usuario
    public void opciones_usuario(View view){

        startActivity(new Intent(this,activity_usuario.class));
    }*/
    public void manual_usuario(View view) {
        startActivity(new Intent(this, activity_manual_usuario.class));
    }
    /*
    public void contribuciones(View view){
        startActivity(new Intent(this,activity_contribuciones.class));
    }

    public void alerta_temprana(View view){
        startActivity(new Intent(this,activity_alerta_temprana.class));
    }
    */

}
