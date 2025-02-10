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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
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
    private TextView nav_recuperar_contrasena;
    private GoogleApiClient mGoogleApiClient;
    private static final int RC_SIGN_IN = 9001;
    private static final String TAG = "SignInActivity";
    GoogleSignInAccount acct = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.login_main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ImageButton btn_iniciar_sesion = findViewById(R.id.btn_iniciar_sesion);
        ImageButton btn_register = findViewById(R.id.btn_nav_registro);
        ImageButton sing_in_button = findViewById(R.id.sing_in_button);
        usernameEditText = findViewById(R.id.email);
        passwordEditText = findViewById(R.id.password);
        nav_recuperar_contrasena = findViewById(R.id.recuperar_contrasena);

        btn_iniciar_sesion.setOnClickListener(view -> {
            String emailUser = usernameEditText.getText().toString().trim();
            String passUser = passwordEditText.getText().toString().trim();

            if (emailUser.isEmpty() || passUser.isEmpty()) {
                Toast.makeText(loginActivity.this, "Please enter your credentials", Toast.LENGTH_SHORT).show();
            } else {
                loginUser(emailUser, passUser);
            }
        });

        btn_register.setOnClickListener(view -> startActivity(new Intent(loginActivity.this, registroActivity.class)));

        nav_recuperar_contrasena.setOnClickListener(view -> startActivity(new Intent(loginActivity.this, recuperar_contrasenia.class)));

        sing_in_button.setOnClickListener(view -> loginWithGoogle());
    }

    private void loginUser(String emailUser, String passUser) {
        mAuth.signInWithEmailAndPassword(emailUser, passUser)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        startActivity(new Intent(loginActivity.this, MainActivity.class));
                        Toast.makeText(loginActivity.this, "Bienvenido!", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(loginActivity.this, "Algo salió mal", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(loginActivity.this, "Error durante el login", Toast.LENGTH_SHORT).show());
    }

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

    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult: " + result.isSuccess());

        if (result.isSuccess()) {
            GoogleSignInAccount acct = result.getSignInAccount();
            assert acct != null;
            Toast.makeText(this, "Bienvenido! " + acct.getDisplayName(), Toast.LENGTH_SHORT).show();
            startActivity(new Intent(loginActivity.this, MainActivity.class));
        } else {
            Status status = result.getStatus();
            int statusCode = status.getStatusCode();
            String statusMessage = status.getStatusMessage();

            Log.e(TAG, "Google Sign-In failed. Status Code: " + statusCode + ", Message: " + statusMessage);
            Toast.makeText(this, "Google Sign-In failed. Status: " + statusMessage, Toast.LENGTH_LONG).show();

            if (statusCode == CommonStatusCodes.SIGN_IN_REQUIRED) {
                Log.d(TAG, "SIGN_IN_REQUIRED");
            } else if (statusCode == CommonStatusCodes.NETWORK_ERROR) {
                Log.d(TAG, "NETWORK_ERROR");
            } else if (statusCode == CommonStatusCodes.DEVELOPER_ERROR) {
                Log.d(TAG, "DEVELOPER_ERROR");
            } else if (statusCode == CommonStatusCodes.INTERNAL_ERROR) {
                Log.d(TAG, "INTERNAL_ERROR");
            } else if (statusCode == CommonStatusCodes.INVALID_ACCOUNT) {
                Log.d(TAG, "INVALID_ACCOUNT");
            }
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "GoogleApiClient connection failed: " + connectionResult.getErrorMessage());
        Toast.makeText(this, "Google API Client connection failed", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        GoogleSignInAccount googleUser = GoogleSignIn.getLastSignedInAccount(this);

        if (firebaseUser != null || googleUser != null) {
            startActivity(new Intent(loginActivity.this, MainActivity.class));
            finish();
        }
    }
}