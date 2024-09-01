package com.example.sberry_ia20;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class loginActivity extends AppCompatActivity {

    private ImageButton btn_iniciar_sesion;
    private ImageButton btn_register;
    private FirebaseAuth mAuth;
    private EditText usernameEditText;
    private EditText passwordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        setContentView(R.layout.activity_login);

        // Ajuste del padding para evitar superposición con los system bars
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.login_main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Inicialización de vistas
        btn_iniciar_sesion = findViewById(R.id.btn_iniciar_sesion);
        btn_register = findViewById(R.id.btn_nav_registro);
        usernameEditText = findViewById(R.id.email);
        passwordEditText = findViewById(R.id.password);

        // Listener para el botón de iniciar sesión
        btn_iniciar_sesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String emailUser = usernameEditText.getText().toString().trim();
                String passUser = passwordEditText.getText().toString().trim();

                if (emailUser.isEmpty() || passUser.isEmpty()) {
                    Toast.makeText(loginActivity.this, "Ingresar los datos", Toast.LENGTH_SHORT).show();
                } else {
                    loginUser(emailUser, passUser);
                }
            }
        });

        // Listener para el botón de registro
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(loginActivity.this, registroActivity.class));
            }
        });

        // Listener para login anónimo (comentado)
        /*btn_login_anonymous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginAnonymous();
            }
        });*/
    }

    private void loginAnonymous() {
        mAuth.signInAnonymously()
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            startActivity(new Intent(loginActivity.this, MainActivity.class));
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(loginActivity.this, "Error al acceder", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void loginUser(String emailUser, String passUser) {
        mAuth.signInWithEmailAndPassword(emailUser, passUser).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    finish();
                    startActivity(new Intent(loginActivity.this, MainActivity.class));
                    Toast.makeText(loginActivity.this, "Bienvenido", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(loginActivity.this, "Error", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(loginActivity.this, "Error al iniciar sesión", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            startActivity(new Intent(loginActivity.this, MainActivity.class));
            finish();
        }
    }
}
