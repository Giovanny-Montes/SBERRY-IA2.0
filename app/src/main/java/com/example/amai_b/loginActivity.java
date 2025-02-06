package com.example.amai_b;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class loginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    private FirebaseAuth mAuth;
    private EditText usernameEditText;
    private EditText passwordEditText;
    private GoogleApiClient mGoogleApiClient;
    private static final int RC_SIGN_IN = 9001;
    private static final String TAG = "SignInActivity";
    GoogleSignInAccount acct =null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialized Firebase Authentication
        mAuth = FirebaseAuth.getInstance();

        // Configured Google Sign-In options
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // Built GoogleApiClient
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        // Adjusted padding for system bars
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.login_main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialized views
        ImageButton btn_iniciar_sesion = findViewById(R.id.btn_iniciar_sesion);
        ImageButton btn_register = findViewById(R.id.btn_nav_registro);
        ImageButton sing_in_button = findViewById(R.id.sing_in_button);
        usernameEditText = findViewById(R.id.email);
        passwordEditText = findViewById(R.id.password);

        // Set listener for the login button
        btn_iniciar_sesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String emailUser = usernameEditText.getText().toString().trim();
                String passUser = passwordEditText.getText().toString().trim();

                if (emailUser.isEmpty() || passUser.isEmpty()) {
                    Toast.makeText(loginActivity.this, "Please enter your credentials", Toast.LENGTH_SHORT).show();
                } else {
                    loginUser(emailUser, passUser);
                }
            }
        });

        // Set listener for the registration button
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(loginActivity.this, registroActivity.class));
            }
        });

        // Set listener for the Google Sign-In button
        sing_in_button.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                loginWithGoogle();
            }
        });
    }

    // Attempted to log in the user with email and password
    private void loginUser(String emailUser, String passUser) {
        mAuth.signInWithEmailAndPassword(emailUser, passUser)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            finish();
                            startActivity(new Intent(loginActivity.this, MainActivity.class));
                            Toast.makeText(loginActivity.this, "Welcome!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(loginActivity.this, "Login failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(loginActivity.this, "Error during login", Toast.LENGTH_SHORT).show();
                    }
                });
    }



    // Inicia todo el modelo de entrada para google
    private void loginWithGoogle() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    // Handled the result of the Google Sign-In attempt
    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult: " + result.isSuccess());
        if (result.isSuccess()) {
            GoogleSignInAccount acct = result.getSignInAccount();
            assert acct != null;
            Toast.makeText(this, "Bienvenido! "+ acct.getDisplayName(), Toast.LENGTH_SHORT).show();
            startActivity(new Intent(loginActivity.this, MainActivity.class));
        } else {
            Toast.makeText(this, "Google Sign-In failed", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        // Handle connection failure here.
        Log.d(TAG, "GoogleApiClient connection failed: " + connectionResult.getErrorMessage());
        Toast.makeText(this, "Google API Client connection failed", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        GoogleSignInAccount googleUser = GoogleSignIn.getLastSignedInAccount(this); // Correct usage

        if (firebaseUser != null || googleUser != null) {
            startActivity(new Intent(loginActivity.this, MainActivity.class));
            finish();
        }
    }

    /*
    @Override
    protected void onResume() {
        super.onResume();
        FirebaseUser user = mAuth.getCurrentUser();

        if (user != null || acct != null) {
            Intent entrar = new Intent(loginActivity.this, MainActivity.class);
            assert user != null;
            entrar.putExtra("Usuario"+user.getDisplayName(),true);
            assert acct != null;
            entrar.putExtra("Usuario"+acct.getDisplayName(),true);
            startActivity(entrar);

            finish();}
        else {
                Toast.makeText(this,"No se encontró ningún usuario",Toast.LENGTH_SHORT).show();
        }
    }
    */


}