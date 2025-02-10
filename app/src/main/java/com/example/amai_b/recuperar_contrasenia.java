package com.example.amai_b;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;

public class recuperar_contrasenia extends AppCompatActivity {

    private EditText etEmail;
    private Button btnRecuperar;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_recuperar_contrasenia);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        etEmail = findViewById(R.id.et_email);
        btnRecuperar = findViewById(R.id.btn_recuperar);
        mAuth = FirebaseAuth.getInstance();

        btnRecuperar.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();

            if (email.isEmpty()) {
                Toast.makeText(recuperar_contrasenia.this, "Ingrese su correo electrónico", Toast.LENGTH_SHORT).show();
                return;
            }

            mAuth.sendPasswordResetEmail(email)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(recuperar_contrasenia.this, "Correo de recuperación enviado", Toast.LENGTH_SHORT).show();
                            finish(); // Cierra la actividad después de enviar el correo
                        } else {
                            String errorMessage = task.getException().getMessage();
                            Toast.makeText(recuperar_contrasenia.this, "Error al enviar correo: " + errorMessage, Toast.LENGTH_SHORT).show();
                        }
                    });
        });
    }
}