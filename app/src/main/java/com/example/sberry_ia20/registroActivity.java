package com.example.sberry_ia20;

import android.content.Intent;
import android.os.Bundle;import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import com.google.firebase.auth.FirebaseAuth;

public class registroActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    Button reg_enviar, reg_regresar;
    EditText reg_correo, reg_con, reg_con2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        mAuth = FirebaseAuth.getInstance();

        setContentView(R.layout.activity_registro);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        reg_correo = findViewById(R.id.reg_correo);
        reg_con = findViewById(R.id.reg_con);
        reg_con2 = findViewById(R.id.reg_con2);
        reg_enviar = findViewById(R.id.reg_enviar);
        reg_regresar=findViewById(R.id.reg_regresar);


        findViewById(R.id.reg_enviar).setOnClickListener(view -> {
            String email = reg_correo.getText().toString();
            String password = reg_con.getText().toString();
            String password2 = reg_con2.getText().toString();
            registrarUsuario(email, password, password2);
        });


       reg_regresar.setOnClickListener(view -> {
            startActivity(new Intent(registroActivity.this, loginActivity.class));
        });
    }

    private void registrarUsuario(String email, String password, String password2) {
        if (email.trim().isEmpty()) {
            Toast.makeText(this, "Ingrese algún correo", Toast.LENGTH_SHORT).show();
            return;
        }
        if (password.trim().isEmpty() || password2.trim().isEmpty()) {
            Toast.makeText(this, "Ingrese alguna contraseña", Toast.LENGTH_SHORT).show();
            return;
        }

        if (password.equals(password2)) {
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(this, "Registro Exitoso, Bienvenido!", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(this, MainActivity.class));
                            finish();
                        } else {
                            Toast.makeText(this, "El Registro falló", Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            Toast.makeText(this, "Contraseñas no coinciden", Toast.LENGTH_SHORT).show();
        }


    }
}